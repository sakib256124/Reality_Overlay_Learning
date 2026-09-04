package com.rola.app.presentation.scanner

import com.rola.app.data.ar.ARInformationNode

enum class ScannerStatus {
    CheckingAvailability,
    WaitingForCameraPermission,
    SearchingObject,
    ObjectDetected,
    Processing,
    Error,
}

data class ScannerUiState(
    val status: ScannerStatus = ScannerStatus.CheckingAvailability,
    val statusMessage: String = "Checking ARCore support...",
    val isArSupported: Boolean = false,
    val hasCameraPermission: Boolean = false,
    val isSessionRunning: Boolean = false,
    val isCameraTracking: Boolean = false,
    val detectedPlaneCount: Int = 0,
    val anchorCount: Int = 0,
    val detectedObjectLabel: String? = null,
    val arInformationNode: ARInformationNode? = null,
    val overlayErrorMessage: String? = null,
    val overlayTrackingLostMessage: String? = null,
    val errorMessage: String? = null,
    val pendingAnchorRequest: Boolean = false,
)
