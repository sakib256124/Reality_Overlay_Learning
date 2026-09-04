package com.rola.app.presentation.visualization

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader

@Composable
fun AR3DViewerScreen(
    onBack: () -> Unit,
    viewModel: VisualizationViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val modelLoader = rememberModelLoader()
    val modelInstance = uiState.modelAsset?.sourcePath?.let { sourcePath ->
        rememberModelInstance(modelLoader, sourcePath)
    }
    var anchor by remember { mutableStateOf<Anchor?>(null) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var viewportWidth by remember { mutableStateOf(0) }
    var viewportHeight by remember { mutableStateOf(0) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            if (!granted) {
                viewModel.onArSessionFailure("Camera permission is required for AR visualization.")
            }
        },
    )

    LaunchedEffect(uiState.status) {
        if (uiState.status == VisualizationStatus.ReadyToPlace) {
            anchor?.detach()
            anchor = null
        }
    }

    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onSizeChanged {
                viewportWidth = it.width
                viewportHeight = it.height
            }
            .pointerInput(uiState.hasModel) {
                detectTransformGestures { _, pan, zoom, rotation ->
                    if (!uiState.hasModel) return@detectTransformGestures
                    viewModel.rotateBy(rotation)
                    viewModel.zoomBy(zoom)
                    viewModel.moveBy(
                        deltaX = pan.x / PAN_TO_METERS,
                        deltaZ = pan.y / PAN_TO_METERS,
                    )
                }
            },
    ) {
        if (hasCameraPermission) {
            ARSceneView(
                modifier = Modifier.fillMaxSize(),
                planeRenderer = true,
                planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL,
                depthMode = Config.DepthMode.AUTOMATIC,
                focusMode = Config.FocusMode.AUTO,
                updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE,
                onSessionFailure = { failure ->
                    viewModel.onArSessionFailure(failure.cause?.message ?: "AR session could not start.")
                },
                onSessionUpdated = { _, frame ->
                    viewModel.onTrackingChanged(frame.camera.trackingState == TrackingState.TRACKING)
                    if (uiState.isPlacementRequested) {
                        val placedAnchor = createCenterAnchor(frame, viewportWidth, viewportHeight)
                        if (placedAnchor == null) {
                            viewModel.onPlacementFailed("Aim at a detected surface before placing the model.")
                        } else {
                            anchor?.detach()
                            anchor = placedAnchor
                            viewModel.onModelPlaced()
                        }
                    }
                    if (anchor?.trackingState == TrackingState.STOPPED) {
                        anchor?.detach()
                        anchor = null
                        viewModel.onPlacementFailed("AR tracking was lost. Place the model again.")
                    }
                },
            ) {
                val activeAnchor = anchor
                val asset = uiState.modelAsset
                if (activeAnchor != null && modelInstance != null && asset != null) {
                    AnchorNode(anchor = activeAnchor) {
                        ModelNode(
                            modelInstance = modelInstance,
                            scaleToUnits = asset.model.scale * uiState.interaction.effectiveScale,
                            autoAnimate = uiState.interaction.isAnimationEnabled,
                            position = Position(
                                x = uiState.interaction.offsetX,
                                y = if (uiState.interaction.isExplodedView) 0.08f else 0f,
                                z = uiState.interaction.offsetZ,
                            ),
                            rotation = Rotation(
                                x = asset.model.rotation.x,
                                y = asset.model.rotation.y + uiState.interaction.rotationYDegrees,
                                z = asset.model.rotation.z,
                            ),
                        )
                    }
                }
            }
        } else {
            PermissionRequiredPanel(
                onRequestPermission = {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                },
                modifier = Modifier.align(Alignment.Center),
            )
        }

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(8.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
            )
        }

        VisualizationHint(
            uiState = uiState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(top = 64.dp, start = 20.dp, end = 20.dp),
        )

        ModelControlPanel(
            uiState = uiState,
            onPlace = viewModel::requestPlacement,
            onRotate = { viewModel.rotateBy(30f) },
            onZoomIn = { viewModel.zoomBy(1.12f) },
            onZoomOut = { viewModel.zoomBy(0.88f) },
            onReset = {
                anchor?.detach()
                anchor = null
                viewModel.resetInteraction()
            },
            onAnimate = viewModel::toggleAnimation,
            onExplode = viewModel::toggleExplodedView,
            onHighlight = viewModel::toggleHighlightMode,
            onInfo = viewModel::speakModelDescription,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(20.dp),
        )
    }
}

@Composable
private fun PermissionRequiredPanel(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(24.dp),
        color = Color.Black.copy(alpha = 0.72f),
        contentColor = Color.White,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        ) {
            Text(text = "Camera permission is required for AR visualization.")
            Button(onClick = onRequestPermission) {
                Text(text = "Allow Camera")
            }
        }
    }
}

@Composable
private fun VisualizationHint(
    uiState: VisualizationUiState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.58f),
        contentColor = Color.White,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Text(
            text = when (uiState.status) {
                VisualizationStatus.Loading -> "Loading 3D model..."
                VisualizationStatus.ReadyToPlace -> "Find a surface and place the model."
                VisualizationStatus.Placed -> "Drag, pinch, or use controls to explore."
                VisualizationStatus.Error -> uiState.errorMessage ?: "3D model unavailable."
            },
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        )
    }
}

private fun createCenterAnchor(
    frame: Frame,
    viewportWidth: Int,
    viewportHeight: Int,
): Anchor? {
    if (viewportWidth <= 0 || viewportHeight <= 0) return null

    return frame.hitTest(viewportWidth / 2f, viewportHeight / 2f)
        .firstOrNull { hit ->
            val trackable = hit.trackable
            trackable is Plane &&
                trackable.trackingState == TrackingState.TRACKING &&
                trackable.isPoseInPolygon(hit.hitPose)
        }
        ?.let { hit -> runCatching { hit.createAnchor() }.getOrNull() }
}

private const val PAN_TO_METERS = 900f
