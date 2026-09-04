package com.rola.app.data.ar

import com.google.ar.core.Anchor
import com.google.ar.core.TrackingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class TrackedAnchor(
    val id: String,
    val anchor: Anchor,
)

@Singleton
class AnchorRepository @Inject constructor() {
    private val anchors = linkedMapOf<String, Anchor>()
    private val _anchorCount = MutableStateFlow(0)
    val anchorCount: StateFlow<Int> = _anchorCount.asStateFlow()

    fun add(anchor: Anchor): TrackedAnchor {
        val trackedAnchor = TrackedAnchor(
            id = UUID.randomUUID().toString(),
            anchor = anchor,
        )
        anchors[trackedAnchor.id] = anchor
        publishAnchorCount()
        return trackedAnchor
    }

    fun getTrackingStates(): Map<String, TrackingState> =
        anchors.mapValues { (_, anchor) -> anchor.trackingState }

    fun getAnchor(anchorId: String): Anchor? = anchors[anchorId]

    fun remove(anchorId: String) {
        anchors.remove(anchorId)?.detach()
        publishAnchorCount()
    }

    fun clear() {
        anchors.values.forEach { anchor -> anchor.detach() }
        anchors.clear()
        publishAnchorCount()
    }

    private fun publishAnchorCount() {
        _anchorCount.value = anchors.size
    }
}
