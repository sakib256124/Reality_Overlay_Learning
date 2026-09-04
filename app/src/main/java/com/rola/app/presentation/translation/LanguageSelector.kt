package com.rola.app.presentation.translation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rola.app.domain.model.Language

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    languages: List<Language>,
    selectedLanguageCode: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = languages.firstOrNull { it.languageCode == selectedLanguageCode }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = selected?.let { "${it.nativeName} (${it.languageName})" } ?: selectedLanguageCode,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Current Language") },
            leadingIcon = { Icon(Icons.Rounded.Language, contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(text = "${language.nativeName} - ${language.languageName}") },
                    onClick = {
                        expanded = false
                        onLanguageSelected(language.languageCode)
                    },
                )
            }
        }
    }
}

