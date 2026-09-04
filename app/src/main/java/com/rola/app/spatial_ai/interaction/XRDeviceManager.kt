package com.rola.app.spatial_ai.interaction

import com.rola.app.domain.model.XRCapability
import com.rola.app.domain.model.XRDeviceProfile
import com.rola.app.domain.model.XRDeviceType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XRDeviceManager @Inject constructor() {
    fun detectDefaultDevice(): XRDeviceProfile = XRDeviceProfile(
        deviceId = "phone-ar-default",
        name = "Android AR Device",
        deviceType = XRDeviceType.PhoneAR,
        connected = true,
        capabilities = setOf(XRCapability.PlaneTracking, XRCapability.Depth, XRCapability.SpatialAudio),
    )

    fun supports(
        device: XRDeviceProfile,
        required: Set<XRCapability>,
    ): Boolean = required.all { it in device.capabilities }

    fun inputProfile(device: XRDeviceProfile): String = when (device.deviceType) {
        XRDeviceType.PhoneAR -> "touch, motion, camera, voice"
        XRDeviceType.ARGlasses -> "voice, gaze, gesture, shared anchors"
        XRDeviceType.VRHeadset -> "controllers, hand tracking, spatial audio"
        XRDeviceType.MixedRealityHeadset -> "hand tracking, depth mesh, spatial anchors"
        XRDeviceType.SpatialComputer -> "eye tracking, hand tracking, spatial windows"
        XRDeviceType.Unknown -> "basic touch input"
    }
}
