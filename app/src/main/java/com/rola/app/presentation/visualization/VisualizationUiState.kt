package com.rola.app.presentation.visualization

import com.rola.app.data.model3d.ResolvedModelAsset
import com.rola.app.domain.model.ModelInteraction

enum class VisualizationStatus {
    Loading,
    ReadyToPlace,
    Placed,
    Error,
}

data class VisualizationUiState(
    val objectId: String = "",
    val modelAsset: ResolvedModelAsset? = null,
    val interaction: ModelInteraction = ModelInteraction(),
    val status: VisualizationStatus = VisualizationStatus.Loading,
    val isTracking: Boolean = false,
    val isPlacementRequested: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasModel: Boolean
        get() = modelAsset != null
}
