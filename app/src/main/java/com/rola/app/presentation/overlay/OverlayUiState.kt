package com.rola.app.presentation.overlay

import com.rola.app.data.ar.AnchorScreenTransform
import com.rola.app.domain.model.Language
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.WearableDisplayMode

enum class OverlayStatus {
    Hidden,
    Loading,
    Visible,
    Error,
    TrackingLost,
}

data class OverlayUiState(
    val status: OverlayStatus = OverlayStatus.Hidden,
    val originalObjectInformation: ObjectInformation? = null,
    val objectInformation: ObjectInformation? = null,
    val screenTransform: AnchorScreenTransform? = null,
    val isExpanded: Boolean = false,
    val languages: List<Language> = emptyList(),
    val selectedLanguageCode: String = "en",
    val displayMode: WearableDisplayMode = WearableDisplayMode.MobileFloatingCard,
    val isTranslationLoading: Boolean = false,
    val translationFromCache: Boolean = false,
    val errorMessage: String? = null,
) {
    val isVisible: Boolean
        get() = status == OverlayStatus.Loading ||
            status == OverlayStatus.Visible ||
            status == OverlayStatus.Error ||
            status == OverlayStatus.TrackingLost

    val isWearableOptimized: Boolean
        get() = displayMode != WearableDisplayMode.MobileFloatingCard
}
