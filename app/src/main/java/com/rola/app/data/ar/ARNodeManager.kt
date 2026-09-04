package com.rola.app.data.ar

import com.google.ar.core.Anchor
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.rola.app.domain.model.RecognitionResult
import javax.inject.Inject
import javax.inject.Singleton

data class ARInformationNode(
    val nodeId: String,
    val objectId: String,
    val objectName: String,
    val confidence: Float,
    val anchorId: String,
    val screenTransform: AnchorScreenTransform? = null,
)

sealed class ARNodeUpdate {
    data class Active(val node: ARInformationNode) : ARNodeUpdate()
    data class TrackingLost(val message: String) : ARNodeUpdate()
    data class Error(val message: String) : ARNodeUpdate()
    data object Empty : ARNodeUpdate()
}

@Singleton
class ARNodeManager @Inject constructor(
    private val anchorRepository: AnchorRepository,
) {
    private val anchorRenderer = AnchorRenderer()
    private var activeNode: ARInformationNode? = null

    fun processDetectedObject(
        session: Session,
        frame: Frame,
        viewportWidth: Int,
        viewportHeight: Int,
        recognitionResult: RecognitionResult?,
        shouldReplaceExisting: Boolean,
    ): ARNodeUpdate {
        removeStoppedAnchors()

        val currentNode = activeNode
        if (currentNode != null && !shouldReplaceExisting) {
            return updateNodeTransform(currentNode, frame, viewportWidth, viewportHeight)
        }

        if (recognitionResult == null) {
            return currentNode?.let { updateNodeTransform(it, frame, viewportWidth, viewportHeight) }
                ?: ARNodeUpdate.Empty
        }

        if (frame.camera.trackingState != TrackingState.TRACKING) {
            return ARNodeUpdate.Error("Camera tracking is unavailable. Move slowly and try again.")
        }

        val anchor = createAnchorAtScreenCenter(frame, viewportWidth, viewportHeight)
            ?: return ARNodeUpdate.Error("Point the reticle at a detected plane before placing the overlay.")

        activeNode?.let { anchorRepository.remove(it.anchorId) }

        val trackedAnchor = anchorRepository.add(anchor)
        val node = ARInformationNode(
            nodeId = trackedAnchor.id,
            objectId = recognitionResult.name.toObjectId(),
            objectName = recognitionResult.name,
            confidence = recognitionResult.confidence,
            anchorId = trackedAnchor.id,
        )
        activeNode = node

        return updateNodeTransform(node, frame, viewportWidth, viewportHeight)
    }

    fun clear() {
        activeNode = null
        anchorRepository.clear()
    }

    fun removeActiveNode() {
        activeNode?.let { anchorRepository.remove(it.anchorId) }
        activeNode = null
    }

    private fun updateNodeTransform(
        node: ARInformationNode,
        frame: Frame,
        viewportWidth: Int,
        viewportHeight: Int,
    ): ARNodeUpdate {
        val anchor = anchorRepository.getAnchor(node.anchorId)
            ?: return ARNodeUpdate.TrackingLost("AR anchor was removed.")

        return when (anchor.trackingState) {
            TrackingState.TRACKING -> {
                val transform = anchorRenderer.projectAnchor(anchor, frame, viewportWidth, viewportHeight)
                val updatedNode = node.copy(screenTransform = transform)
                activeNode = updatedNode
                ARNodeUpdate.Active(updatedNode)
            }
            TrackingState.PAUSED -> ARNodeUpdate.Active(node.copy(screenTransform = node.screenTransform))
            TrackingState.STOPPED -> {
                removeActiveNode()
                ARNodeUpdate.TrackingLost("Object tracking was lost. Rescan the object.")
            }
        }
    }

    private fun createAnchorAtScreenCenter(
        frame: Frame,
        viewportWidth: Int,
        viewportHeight: Int,
    ): Anchor? {
        if (viewportWidth <= 0 || viewportHeight <= 0) return null

        return frame
            .hitTest(viewportWidth / 2f, viewportHeight / 2f)
            .firstOrNull { hit ->
                val trackable = hit.trackable
                trackable is Plane &&
                    trackable.trackingState == TrackingState.TRACKING &&
                    trackable.isPoseInPolygon(hit.hitPose) &&
                    trackable.type in supportedPlaneTypes
            }
            ?.let { hit -> runCatching { hit.createAnchor() }.getOrNull() }
    }

    private fun removeStoppedAnchors() {
        anchorRepository.getTrackingStates()
            .filterValues { trackingState -> trackingState == TrackingState.STOPPED }
            .keys
            .forEach(anchorRepository::remove)
    }

    private fun String.toObjectId(): String = trim()
        .lowercase()
        .replace(Regex("[^a-z0-9]+"), "_")
        .trim('_')
        .ifBlank { "unknown" }

    private companion object {
        val supportedPlaneTypes = setOf(
            Plane.Type.HORIZONTAL_UPWARD_FACING,
            Plane.Type.HORIZONTAL_DOWNWARD_FACING,
            Plane.Type.VERTICAL,
        )
    }
}
