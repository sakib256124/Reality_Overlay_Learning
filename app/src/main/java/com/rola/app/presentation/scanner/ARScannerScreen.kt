package com.rola.app.presentation.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.RadioButtonChecked
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.Config
import com.rola.app.presentation.overlay.ARInformationOverlay
import com.rola.app.presentation.overlay.OverlayViewModel
import com.rola.app.presentation.recognition.RecognitionResultCard
import com.rola.app.presentation.recognition.RecognitionStatus
import com.rola.app.presentation.recognition.RecognitionViewModel
import com.rola.app.presentation.voice.VoiceControlPanel
import com.rola.app.presentation.voice.VoiceViewModel
import io.github.sceneview.ar.ARSceneView

@Composable
fun ARScannerScreen(
    onBack: () -> Unit,
    viewModel: ARScannerViewModel = hiltViewModel(),
    recognitionViewModel: RecognitionViewModel = hiltViewModel(),
    overlayViewModel: OverlayViewModel = hiltViewModel(),
    voiceViewModel: VoiceViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val recognitionUiState by recognitionViewModel.uiState.collectAsState()
    val overlayUiState by overlayViewModel.uiState.collectAsState()
    val voiceUiState by voiceViewModel.uiState.collectAsState()
    var viewportWidth by remember { mutableIntStateOf(0) }
    var viewportHeight by remember { mutableIntStateOf(0) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = viewModel::onCameraPermissionResult,
    )

    LaunchedEffect(Unit) {
        viewModel.checkArAvailability()
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            viewModel.onCameraPermissionResult(true)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(uiState.arInformationNode) {
        uiState.arInformationNode?.let(overlayViewModel::onNodeUpdated)
    }

    LaunchedEffect(uiState.overlayErrorMessage) {
        uiState.overlayErrorMessage?.let(overlayViewModel::onPlacementError)
    }

    LaunchedEffect(uiState.overlayTrackingLostMessage) {
        uiState.overlayTrackingLostMessage?.let(overlayViewModel::onTrackingLost)
    }

    LaunchedEffect(
        overlayUiState.objectInformation?.objectId,
        overlayUiState.objectInformation?.name,
        overlayUiState.objectInformation?.description,
        overlayUiState.selectedLanguageCode,
    ) {
        voiceViewModel.onObjectInformationChanged(overlayUiState.objectInformation)
    }

    DisposableEffect(Unit) {
        onDispose { voiceViewModel.stop() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onSizeChanged {
                viewportWidth = it.width
                viewportHeight = it.height
            },
    ) {
        if (uiState.hasCameraPermission && uiState.isArSupported) {
            ARSceneView(
                modifier = Modifier.fillMaxSize(),
                planeRenderer = true,
                planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL,
                depthMode = Config.DepthMode.AUTOMATIC,
                focusMode = Config.FocusMode.AUTO,
                updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE,
                sessionConfiguration = viewModel::configureSession,
                onSessionCreated = { viewModel.onSessionCreated() },
                onSessionResumed = { session -> viewModel.onSessionResumed(session) },
                onSessionPaused = { viewModel.onSessionPaused() },
                onSessionFailure = { failure ->
                    viewModel.onSessionFailure(failure.cause)
                },
                onSessionUpdated = { session, frame ->
                    recognitionViewModel.onCameraFrame(frame)
                    viewModel.onFrameUpdated(
                        session = session,
                        frame = frame,
                        viewportWidth = viewportWidth,
                        viewportHeight = viewportHeight,
                        recognitionResult = recognitionUiState.latestResult
                            ?.takeIf { recognitionUiState.status == RecognitionStatus.ObjectDetected },
                    )
                },
            )
        }

        ScannerReticle(
            modifier = Modifier.align(Alignment.Center),
            isProcessing = uiState.status == ScannerStatus.Processing,
        )

        ScannerTopBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            onBack = onBack,
            isCameraTracking = uiState.isCameraTracking,
            planeCount = uiState.detectedPlaneCount,
            anchorCount = uiState.anchorCount,
        )

        RecognitionResultCard(
            uiState = recognitionUiState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(start = 20.dp, top = 76.dp, end = 20.dp),
        )

        ARInformationOverlay(
            uiState = overlayUiState,
            onPanelTapped = overlayViewModel::onPanelTapped,
            onClose = {
                voiceViewModel.stop()
                overlayViewModel.closeOverlay()
                viewModel.closeOverlay()
            },
            onLanguageSelected = overlayViewModel::onLanguageSelected,
            modifier = Modifier.fillMaxSize(),
        )

        VoiceControlPanel(
            uiState = voiceUiState,
            onPlay = voiceViewModel::play,
            onPause = voiceViewModel::pause,
            onResume = voiceViewModel::resume,
            onStop = voiceViewModel::stop,
            onSpeechRateChanged = voiceViewModel::onSpeechRateChanged,
            onPitchChanged = voiceViewModel::onPitchChanged,
            onLanguageSelected = voiceViewModel::onLanguageSelected,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(start = 20.dp, end = 20.dp, bottom = 164.dp),
        )

        ScannerControls(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(20.dp),
            uiState = uiState,
            onScanClicked = {
                voiceViewModel.stop()
                overlayViewModel.prepareForRescan()
                viewModel.onScanClicked()
            },
            onClearClicked = {
                voiceViewModel.stop()
                overlayViewModel.closeOverlay()
                viewModel.clearAnchors()
            },
            onRequestPermission = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
        )
    }
}

@Composable
private fun ScannerTopBar(
    onBack: () -> Unit,
    isCameraTracking: Boolean,
    planeCount: Int,
    anchorCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
            )
        }
        Surface(
            color = Color.Black.copy(alpha = 0.52f),
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusDot(isActive = isCameraTracking)
                Text(
                    text = "Planes $planeCount  Anchors $anchorCount",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun ScannerControls(
    uiState: ScannerUiState,
    onScanClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Black.copy(alpha = 0.62f),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = uiState.statusMessage,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color(0xFFFFC8C2),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!uiState.hasCameraPermission) {
                    Button(
                        onClick = onRequestPermission,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = "Allow Camera")
                    }
                } else {
                    Button(
                        onClick = onScanClicked,
                        enabled = uiState.isArSupported &&
                            uiState.isSessionRunning &&
                            uiState.detectedPlaneCount > 0,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.RadioButtonChecked,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Scan")
                    }
                    FilledTonalButton(
                        onClick = onClearClicked,
                        enabled = uiState.anchorCount > 0,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                        )
                    }
                }
            }
            uiState.detectedObjectLabel?.let { label ->
                Text(
                    text = label,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun ScannerReticle(
    isProcessing: Boolean,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "scanner-reticle")
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "scanner-sweep",
    )
    val alpha by transition.animateFloat(
        initialValue = 0.38f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "scanner-alpha",
    )

    Canvas(
        modifier = modifier
            .size(160.dp)
            .clip(CircleShape),
    ) {
        val strokeWidth = 3.dp.toPx()
        drawCircle(
            color = Color.White.copy(alpha = 0.28f),
            radius = size.minDimension / 2.4f,
            style = Stroke(width = strokeWidth),
        )
        drawArc(
            color = Color(0xFF62D6A4).copy(alpha = if (isProcessing) alpha else 0.58f),
            startAngle = sweep,
            sweepAngle = 80f,
            useCenter = false,
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round),
        )
        drawLine(
            color = Color.White.copy(alpha = 0.42f),
            start = Offset(size.width * 0.5f, size.height * 0.36f),
            end = Offset(size.width * 0.5f, size.height * 0.64f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Color.White.copy(alpha = 0.42f),
            start = Offset(size.width * 0.36f, size.height * 0.5f),
            end = Offset(size.width * 0.64f, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun StatusDot(isActive: Boolean) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(if (isActive) Color(0xFF62D6A4) else Color(0xFFE0A84E)),
    )
}
