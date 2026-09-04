package com.rola.app.unit

import com.rola.app.data.wearable.WearableConnectionResult
import com.rola.app.data.wearable.WearableLearningFrame
import com.rola.app.data.wearable.WearableManager
import com.rola.app.domain.model.DeviceConnectionType
import com.rola.app.domain.model.DeviceState
import com.rola.app.domain.model.WearableCommand
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WearableManagerTest {
    @Test
    fun discoverDevices_returnsBluetoothAndWifiReferenceDevices() = runTest {
        val manager = WearableManager()

        val devices = manager.discoverDevices(
            setOf(DeviceConnectionType.Bluetooth, DeviceConnectionType.WiFi),
        )

        assertTrue(devices.any { it.connectionType == DeviceConnectionType.Bluetooth })
        assertTrue(devices.any { it.connectionType == DeviceConnectionType.WiFi })
    }

    @Test
    fun connect_establishesTrustedDeviceAndAllowsCommands() = runTest {
        val manager = WearableManager()
        val device = manager.discoverDevices(setOf(DeviceConnectionType.Bluetooth)).first()

        val connectResult = manager.connect(device)
        val commandResult = manager.sendCommand(WearableCommand.ScanObject)

        assertEquals(WearableConnectionResult.Success, connectResult)
        assertEquals(WearableConnectionResult.Success, commandResult)
        assertEquals(DeviceState.Connected, manager.connectionState.value)
        assertTrue(manager.connectedDevice.value?.isTrusted == true)
    }

    @Test
    fun sendLearningFrame_requiresConnectedDevice() = runTest {
        val manager = WearableManager()

        val result = manager.sendLearningFrame(
            WearableLearningFrame(
                objectName = "Apple",
                summary = "A fruit with vitamins.",
                languageCode = "en",
                displayMode = "CompactHeadsUp",
                shouldSpeak = true,
            ),
        )

        assertTrue(result is WearableConnectionResult.Error)
    }
}
