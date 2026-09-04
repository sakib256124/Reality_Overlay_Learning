package com.rola.app.presentation.chatbot

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatInputField(
    value: String,
    canSend: Boolean,
    onValueChanged: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = "Ask ROLA Tutor") },
            minLines = 1,
            maxLines = 4,
            trailingIcon = {
                IconButton(onClick = {}, enabled = false) {
                    Icon(Icons.Rounded.Mic, contentDescription = "Voice input coming soon")
                }
            },
        )
        IconButton(
            onClick = onSend,
            enabled = canSend,
        ) {
            Icon(Icons.Rounded.Send, contentDescription = "Send question")
        }
    }
}
