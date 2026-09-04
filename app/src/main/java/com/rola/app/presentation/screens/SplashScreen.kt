package com.rola.app.presentation.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1_400)
        onFinished()
    }

    val transition = rememberInfiniteTransition(label = "splash")
    val pulse by transition.animateFloat(
        initialValue = 0.72f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(112.dp)) {
                drawCircle(
                    color = androidx.compose.ui.graphics.Color(0xFF2E7D63).copy(alpha = pulse),
                    radius = size.minDimension / 2.4f,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round),
                )
                drawLine(
                    color = androidx.compose.ui.graphics.Color(0xFF4B6F9F),
                    start = Offset(size.width * 0.28f, size.height * 0.5f),
                    end = Offset(size.width * 0.72f, size.height * 0.5f),
                    strokeWidth = 7.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = androidx.compose.ui.graphics.Color(0xFFA15C38),
                    start = Offset(size.width * 0.5f, size.height * 0.28f),
                    end = Offset(size.width * 0.5f, size.height * 0.72f),
                    strokeWidth = 7.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
        }
        Spacer(modifier = Modifier.size(24.dp))
        Text(text = "ROLA", style = MaterialTheme.typography.headlineLarge)
        Text(
            text = "Reality Overlay Learning App",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.size(28.dp))
        CircularProgressIndicator()
    }
}
