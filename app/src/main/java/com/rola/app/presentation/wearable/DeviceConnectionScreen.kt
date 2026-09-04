package com.rola.app.presentation.wearable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rola.app.domain.model.DeviceConnectionType
import com.rola.app.domain.model.DeviceState
import com.rola.app.domain.model.WearableDevice

@Composable
fun DeviceConnectionScreen(
    uiState: WearableUiState,
    onDiscover: () -> Unit,
    onConnect: (WearableDevice) -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = uiState.connectionLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = uiState.connectionState.name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (uiState.isConnected) {
                OutlinedButton(onClick = onDisconnect) {
                    Text(text = "Disconnect")
                }
            } else {
                Button(onClick = onDiscover, enabled = uiState.status != WearableStatus.Discovering) {
                    Text(text = "Discover")
                }
            }
        }

        if (uiState.status == WearableStatus.Discovering || uiState.status == WearableStatus.Connecting) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator()
                Text(text = if (uiState.status == WearableStatus.Discovering) "Finding devices" else "Pairing securely")
            }
        }

        uiState.discoveredDevices.forEach { device ->
            WearableDeviceCard(
                device = device,
                isConnected = uiState.connectedDevice?.deviceId == device.deviceId,
                onConnect = { onConnect(device) },
            )
        }
    }
}

@Composable
private fun WearableDeviceCard(
    device: WearableDevice,
    isConnected: Boolean,
    onConnect: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = device.deviceName, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = device.manufacturer,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Icon(
                    imageVector = if (isConnected) Icons.Rounded.CheckCircle else connectionIcon(device.connectionType),
                    contentDescription = null,
                    tint = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(text = device.displayCapability.name) })
                AssistChip(onClick = {}, label = { Text(text = device.audioCapability.name) })
                AssistChip(
                    onClick = {},
                    leadingIcon = { Icon(Icons.Rounded.BatteryFull, contentDescription = null) },
                    label = { Text(text = "${device.normalizedBatteryLevel}%") },
                )
            }

            Button(
                onClick = onConnect,
                enabled = device.state != DeviceState.Connecting && !isConnected,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = if (isConnected) "Connected" else "Connect")
            }
        }
    }
}

private fun connectionIcon(connectionType: DeviceConnectionType) =
    when (connectionType) {
        DeviceConnectionType.WiFi -> Icons.Rounded.Wifi
        else -> Icons.Rounded.Bluetooth
    }
