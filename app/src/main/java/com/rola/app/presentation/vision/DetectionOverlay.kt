package com.rola.app.presentation.vision

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.rola.app.domain.model.DetectedObject

@Composable
fun DetectionOverlay(
    objects: List<DetectedObject>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val labelColor = MaterialTheme.colorScheme.primary
        Canvas(modifier = Modifier.fillMaxSize()) {
            objects.forEach { detectedObject ->
                val box = detectedObject.boundingBox
                val left = box.left * size.width
                val top = box.top * size.height
                val width = box.width * size.width
                val height = box.height * size.height

                drawRect(
                    color = labelColor,
                    topLeft = Offset(left, top),
                    size = Size(width, height),
                    style = Stroke(width = 4.dp.toPx()),
                )

                drawContext.canvas.nativeCanvas.drawText(
                    "${detectedObject.label} ${(detectedObject.confidence * 100).toInt()}%",
                    left,
                    (top - 10.dp.toPx()).coerceAtLeast(24.dp.toPx()),
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 16.dp.toPx()
                        isFakeBoldText = true
                        setShadowLayer(6f, 0f, 0f, android.graphics.Color.BLACK)
                    },
                )
            }
        }

        if (objects.isEmpty()) {
            Surface(
                color = Color.Black.copy(alpha = 0.42f),
                tonalElevation = 0.dp,
            ) {
                Text(text = "No stable objects")
            }
        }
    }
}
