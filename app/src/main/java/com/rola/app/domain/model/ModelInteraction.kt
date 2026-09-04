package com.rola.app.domain.model

data class ModelInteraction(
    val scaleMultiplier: Float = 1f,
    val rotationYDegrees: Float = 0f,
    val offsetX: Float = 0f,
    val offsetZ: Float = 0f,
    val isExplodedView: Boolean = false,
    val isHighlightMode: Boolean = false,
    val isAnimationEnabled: Boolean = true,
) {
    val effectiveScale: Float
        get() = scaleMultiplier.coerceIn(MIN_SCALE_MULTIPLIER, MAX_SCALE_MULTIPLIER)

    companion object {
        const val MIN_SCALE_MULTIPLIER = 0.35f
        const val MAX_SCALE_MULTIPLIER = 2.4f
    }
}
