package com.rola.app.presentation.vision

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BatterySaver
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.Config
import com.rola.app.presentation.scanner.ARScannerViewModel
import io.github.sceneview.ar.ARSceneView

@Composable
fun VisionScannerScreen(
    onBack: () -> Unit,
    viewModel: VisionViewModel = hiltViewModel(),
    arScannerViewModel: ARScannerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scannerState by arScannerViewModel.uiState.collectAsState()
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = arScannerViewModel::onCameraPermissionResult,
    )

    LaunchedEffect(Unit) {
        arScannerViewModel.checkArAvailability()
        val isGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            arScannerViewModel.onCameraPermissionResult(true)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        if (scannerState.hasCameraPermission && scannerState.isArSupported) {
            ARSceneView(
                modifier = Modifier.fillMaxSize(),
                planeRenderer = false,
                planeFindingMode = Config.PlaneFindingMode.DISABLED,
                depthMode = Config.DepthMode.AUTOMATIC,
                focusMode = Config.FocusMode.AUTO,
                updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE,
                sessionConfiguration = arScannerViewModel::configureSession,
                onSessionCreated = { arScannerViewModel.onSessionCreated() },
                onSessionResumed = { session -> arScannerViewModel.onSessionResumed(session) },
                onSessionPaused = { arScannerViewModel.onSessionPaused() },
                onSessionFailure = { failure -> arScannerViewModel.onSessionFailure(failure.cause) },
                onSessionUpdated = { _, frame -> viewModel.onCameraFrame(frame) },
            )
        }

        DetectionOverlay(
            objects = uiState.trackedObjects,
            modifier = Modifier.fillMaxSize(),
        )

        VisionTopBar(
            statusMessage = uiState.statusMessage,
            lowPowerMode = uiState.lowPowerMode,
            onBack = onBack,
            onReset = viewModel::reset,
            onToggleLowPower = viewModel::toggleLowPowerMode,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing),
        )

        SceneInformationCard(
            uiState = uiState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(16.dp),
        )
    }
}

@Composable
private fun VisionTopBar(
    statusMessage: String,
    lowPowerMode: Boolean,
    onBack: () -> Unit,
    onReset: () -> Unit,
    onToggleLowPower: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.52f),
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = statusMessage,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onToggleLowPower) {
                    Icon(
                        Icons.Rounded.BatterySaver,
                        contentDescription = "Toggle low power mode",
                        tint = if (lowPowerMode) MaterialTheme.colorScheme.primary else Color.White,
                    )
                }
                IconButton(onClick = onReset) {
                    Icon(Icons.Rounded.Refresh, contentDescription = "Reset tracking", tint = Color.White)
                }
            }
        }
    }
}
