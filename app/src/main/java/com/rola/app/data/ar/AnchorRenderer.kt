package com.rola.app.data.ar

import android.opengl.Matrix
import com.google.ar.core.Anchor
import com.google.ar.core.Frame
import com.google.ar.core.TrackingState
import kotlin.math.sqrt

data class AnchorScreenTransform(
    val x: Float,
    val y: Float,
    val distanceMeters: Float,
    val scale: Float,
    val isVisible: Boolean,
)

class AnchorRenderer {
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val anchorMatrix = FloatArray(16)
    private val modelViewMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)
    private val worldPosition = floatArrayOf(0f, PANEL_VERTICAL_OFFSET_METERS, 0f, 1f)
    private val clipPosition = FloatArray(4)
    private val cameraPosition = FloatArray(3)

    fun projectAnchor(
        anchor: Anchor,
        frame: Frame,
        viewportWidth: Int,
        viewportHeight: Int,
    ): AnchorScreenTransform? {
        if (viewportWidth <= 0 || viewportHeight <= 0) return null
        if (anchor.trackingState != TrackingState.TRACKING) return null

        frame.camera.getProjectionMatrix(projectionMatrix, 0, NEAR_CLIP_METERS, FAR_CLIP_METERS)
        frame.camera.getViewMatrix(viewMatrix, 0)
        frame.camera.pose.translation.copyInto(cameraPosition)
        anchor.pose.toMatrix(anchorMatrix, 0)

        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, anchorMatrix, 0)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)
        Matrix.multiplyMV(clipPosition, 0, modelViewProjectionMatrix, 0, worldPosition, 0)

        val w = clipPosition[3]
        if (w <= 0f) {
            return AnchorScreenTransform(0f, 0f, distanceMeters(anchor), scale = 0f, isVisible = false)
        }

        val normalizedX = clipPosition[0] / w
        val normalizedY = clipPosition[1] / w
        val x = ((normalizedX + 1f) * 0.5f) * viewportWidth
        val y = ((1f - normalizedY) * 0.5f) * viewportHeight
        val distance = distanceMeters(anchor)

        return AnchorScreenTransform(
            x = x,
            y = y,
            distanceMeters = distance,
            scale = scaleForDistance(distance),
            isVisible = normalizedX in -1.2f..1.2f && normalizedY in -1.2f..1.2f,
        )
    }

    private fun distanceMeters(anchor: Anchor): Float {
        val anchorPosition = anchor.pose.translation
        val dx = anchorPosition[0] - cameraPosition[0]
        val dy = anchorPosition[1] - cameraPosition[1]
        val dz = anchorPosition[2] - cameraPosition[2]
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    private fun scaleForDistance(distanceMeters: Float): Float =
        (1.25f / distanceMeters.coerceAtLeast(0.55f)).coerceIn(0.72f, 1.18f)

    private companion object {
        const val PANEL_VERTICAL_OFFSET_METERS = 0.18f
        const val NEAR_CLIP_METERS = 0.1f
        const val FAR_CLIP_METERS = 100f
    }
}
