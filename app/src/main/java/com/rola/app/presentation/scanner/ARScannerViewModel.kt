package com.rola.app.presentation.scanner

import androidx.lifecycle.ViewModel
import com.google.ar.core.Frame
import com.google.ar.core.Session
import com.rola.app.data.ar.ARNodeManager
import com.rola.app.data.ar.ARNodeUpdate
import com.rola.app.data.ar.ARAvailability
import com.rola.app.data.ar.ARSessionManager
import com.rola.app.domain.model.DetectedObject
import com.rola.app.domain.model.LearningStatus
import com.rola.app.domain.model.RecognitionResult
import com.rola.app.domain.repository.LearningHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

@HiltViewModel
class ARScannerViewModel @Inject constructor(
    private val arSessionManager: ARSessionManager,
    private val arNodeManager: ARNodeManager,
    private val learningHistoryRepository: LearningHistoryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private var latestDetectedObject: DetectedObject? = null
    private var lastSavedNodeId: String? = null

    fun checkArAvailability() {
        when (arSessionManager.checkAvailability()) {
            ARAvailability.Supported,
            ARAvailability.NeedsInstall,
            -> _uiState.update {
                it.copy(
                    isArSupported = true,
                    status = ScannerStatus.WaitingForCameraPermission,
                    statusMessage = "Waiting for camera permission...",
                    errorMessage = null,
                )
            }
            ARAvailability.Unsupported -> showError("ARCore is not supported on this device.")
            ARAvailability.Checking -> _uiState.update {
                it.copy(
                    status = ScannerStatus.CheckingAvailability,
                    statusMessage = "Checking ARCore support...",
                )
            }
            ARAvailability.Unknown -> showError("Unable to verify ARCore support.")
        }
    }

    fun onCameraPermissionResult(isGranted: Boolean) {
        _uiState.update {
            if (isGranted) {
                it.copy(
                    hasCameraPermission = true,
                    status = ScannerStatus.SearchingObject,
                    statusMessage = "Searching object...",
                    errorMessage = null,
                )
            } else {
                it.copy(
                    hasCameraPermission = false,
                    status = ScannerStatus.Error,
                    statusMessage = "Camera permission denied",
                    errorMessage = "Camera permission is required to start AR scanning.",
                )
            }
        }
    }

    fun configureSession(session: Session, config: com.google.ar.core.Config) {
        runCatching { arSessionManager.configureSession(session, config) }
            .onFailure { throwable -> showError(throwable.message ?: "Unable to configure AR session.") }
    }

    fun onSessionCreated() {
        _uiState.update {
            it.copy(
                isSessionRunning = true,
                status = ScannerStatus.SearchingObject,
                statusMessage = "Searching object...",
                errorMessage = null,
            )
        }
    }

    fun onSessionResumed(session: Session) {
        runCatching { arSessionManager.onSessionResumed(session) }
            .onFailure { throwable -> showError(throwable.message ?: "AR session resume failed.") }
    }

    fun onSessionPaused() {
        _uiState.update {
            it.copy(
                isSessionRunning = false,
                statusMessage = "AR session paused",
            )
        }
    }

    fun onFrameUpdated(
        session: Session,
        frame: Frame,
        viewportWidth: Int,
        viewportHeight: Int,
        recognitionResult: RecognitionResult?,
    ) {
        val currentState = _uiState.value
        val result = runCatching {
            arSessionManager.processFrame(
                session = session,
                frame = frame,
                viewportWidth = viewportWidth,
                viewportHeight = viewportHeight,
                shouldCreateAnchor = false,
            )
        }.getOrElse { throwable ->
            showError(throwable.message ?: "AR frame processing failed.")
            return
        }

        val nodeUpdate = runCatching {
            arNodeManager.processDetectedObject(
                session = session,
                frame = frame,
                viewportWidth = viewportWidth,
                viewportHeight = viewportHeight,
                recognitionResult = recognitionResult,
                shouldReplaceExisting = currentState.pendingAnchorRequest,
            )
        }.getOrElse { throwable ->
            showError(throwable.message ?: "AR overlay processing failed.")
            return
        }

        val activeNode = (nodeUpdate as? ARNodeUpdate.Active)?.node
        if (activeNode != null && activeNode.nodeId != lastSavedNodeId) {
            lastSavedNodeId = activeNode.nodeId
            viewModelScope.launch {
                runCatching {
                    learningHistoryRepository.saveScan(
                        objectId = activeNode.objectId,
                        objectName = activeNode.objectName,
                        category = "Recognized Object",
                        confidenceScore = activeNode.confidence,
                        learningStatus = LearningStatus.Scanned,
                    )
                }
            }
        }

        val detectedObject = activeNode?.let { node ->
            DetectedObject(
                id = node.objectId,
                label = node.objectName,
                confidence = node.confidence,
                anchorId = node.anchorId,
            )
        } ?: latestDetectedObject

        latestDetectedObject = detectedObject

        _uiState.update {
            val hasDetection = detectedObject != null
            val overlayError = (nodeUpdate as? ARNodeUpdate.Error)?.message
            val trackingLost = (nodeUpdate as? ARNodeUpdate.TrackingLost)?.message
            it.copy(
                isCameraTracking = result.isCameraTracking,
                detectedPlaneCount = result.detectedPlaneCount,
                anchorCount = arSessionManager.anchorCount(),
                pendingAnchorRequest = false,
                detectedObjectLabel = detectedObject?.label,
                arInformationNode = activeNode ?: it.arInformationNode.takeUnless { trackingLost != null },
                overlayErrorMessage = overlayError,
                overlayTrackingLostMessage = trackingLost,
                status = if (hasDetection) ScannerStatus.ObjectDetected else ScannerStatus.SearchingObject,
                statusMessage = when {
                    overlayError != null -> overlayError
                    trackingLost != null -> trackingLost
                    hasDetection -> "Object detected"
                    else -> "Searching object..."
                },
                errorMessage = null,
            )
        }
    }

    fun onScanClicked() {
        _uiState.update {
            it.copy(
                pendingAnchorRequest = true,
                status = ScannerStatus.Processing,
                statusMessage = "Processing...",
                overlayErrorMessage = null,
                overlayTrackingLostMessage = null,
            )
        }
    }

    fun onSessionFailure(throwable: Throwable) {
        showError(throwable.message ?: "AR session failed.")
    }

    fun clearAnchors() {
        arNodeManager.clear()
        latestDetectedObject = null
        lastSavedNodeId = null
        _uiState.update {
            it.copy(
                anchorCount = 0,
                detectedObjectLabel = null,
                arInformationNode = null,
                overlayErrorMessage = null,
                overlayTrackingLostMessage = null,
                status = ScannerStatus.SearchingObject,
                statusMessage = "Searching object...",
            )
        }
    }

    fun closeOverlay() {
        arNodeManager.removeActiveNode()
        latestDetectedObject = null
        lastSavedNodeId = null
        _uiState.update {
            it.copy(
                anchorCount = 0,
                detectedObjectLabel = null,
                arInformationNode = null,
                overlayErrorMessage = null,
                overlayTrackingLostMessage = null,
                status = ScannerStatus.SearchingObject,
                statusMessage = "Searching object...",
            )
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                status = ScannerStatus.Error,
                statusMessage = message,
                errorMessage = message,
                pendingAnchorRequest = false,
            )
        }
    }

    override fun onCleared() {
        arNodeManager.clear()
        super.onCleared()
    }
}
