package com.rola.app.data.ar

import android.content.Context
import com.google.ar.core.Anchor
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

enum class ARAvailability {
    Supported,
    NeedsInstall,
    Unsupported,
    Checking,
    Unknown,
}

data class ARFrameResult(
    val isCameraTracking: Boolean,
    val detectedPlaneCount: Int,
    val anchorCount: Int,
    val createdAnchorId: String? = null,
)

@Singleton
class ARSessionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val anchorRepository: AnchorRepository,
) {
    fun checkAvailability(): ARAvailability {
        return when (ArCoreApk.getInstance().checkAvailability(context)) {
            ArCoreApk.Availability.SUPPORTED_INSTALLED -> ARAvailability.Supported
            ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD,
            ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED,
            -> ARAvailability.NeedsInstall
            ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE -> ARAvailability.Unsupported
            ArCoreApk.Availability.UNKNOWN_CHECKING -> ARAvailability.Checking
            ArCoreApk.Availability.UNKNOWN_ERROR,
            ArCoreApk.Availability.UNKNOWN_TIMED_OUT,
            -> ARAvailability.Unknown
        }
    }

    fun configureSession(session: Session, config: Config) {
        config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
        config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
        config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
        config.focusMode = Config.FocusMode.AUTO

        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            config.depthMode = Config.DepthMode.AUTOMATIC
        }
    }

    fun onSessionResumed(session: Session) {
        // SceneView owns the render loop; this hook keeps future session observers centralized.
        session.allAnchors.forEach { anchor ->
            if (anchor.trackingState == TrackingState.STOPPED) {
                anchor.detach()
            }
        }
    }

    fun processFrame(
        session: Session,
        frame: Frame,
        viewportWidth: Int,
        viewportHeight: Int,
        shouldCreateAnchor: Boolean,
    ): ARFrameResult {
        val cameraTracking = frame.camera.trackingState == TrackingState.TRACKING
        val planes = session.getAllTrackables(Plane::class.java)
            .filter { plane ->
                plane.trackingState == TrackingState.TRACKING &&
                    plane.subsumedBy == null &&
                    plane.type in supportedPlaneTypes
            }

        val createdAnchorId = if (
            shouldCreateAnchor &&
            cameraTracking &&
            viewportWidth > 0 &&
            viewportHeight > 0
        ) {
            createAnchorFromScreenCenter(frame, viewportWidth, viewportHeight)?.id
        } else {
            null
        }

        return ARFrameResult(
            isCameraTracking = cameraTracking,
            detectedPlaneCount = planes.size,
            anchorCount = anchorRepository.anchorCount.value,
            createdAnchorId = createdAnchorId,
        )
    }

    fun removeAnchor(anchorId: String) {
        anchorRepository.remove(anchorId)
    }

    fun clearAnchors() {
        anchorRepository.clear()
    }

    fun anchorCount(): Int = anchorRepository.anchorCount.value

    private fun createAnchorFromScreenCenter(
        frame: Frame,
        viewportWidth: Int,
        viewportHeight: Int,
    ): TrackedAnchor? {
        val hitResult = frame
            .hitTest(viewportWidth / 2f, viewportHeight / 2f)
            .firstOrNull { hit ->
                val trackable = hit.trackable
                trackable is Plane &&
                    trackable.isPoseInPolygon(hit.hitPose) &&
                    trackable.type in supportedPlaneTypes
            }

        return hitResult?.createAnchorSafely()?.let(anchorRepository::add)
    }

    private fun com.google.ar.core.HitResult.createAnchorSafely(): Anchor? =
        runCatching { createAnchor() }.getOrNull()

    private companion object {
        val supportedPlaneTypes = setOf(
            Plane.Type.HORIZONTAL_UPWARD_FACING,
            Plane.Type.HORIZONTAL_DOWNWARD_FACING,
            Plane.Type.VERTICAL,
        )
    }
}
