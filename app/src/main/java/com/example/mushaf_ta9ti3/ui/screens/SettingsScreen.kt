package com.example.mushaf_ta9ti3.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import com.example.mushaf_ta9ti3.view.SettingsViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mushaf_ta9ti3.enum.MushafFont

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onReturnHome: () -> Unit,
    onApply: () -> Unit = onReturnHome
) {
    val savedFont by viewModel.currentFont.collectAsState()
    var selectedFont by remember(savedFont) { mutableStateOf(savedFont) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp),) {
        Text(text = "Font Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { selectedFont = MushafFont.STANDARD })
        {
            RadioButton(
                selected = selectedFont == MushafFont.STANDARD,
                onClick = { selectedFont = MushafFont.STANDARD }
            )
            Text(text = "Standard Font")
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { selectedFont = MushafFont.DIGITAL_KHATT })
        {
            RadioButton(
                selected = selectedFont == MushafFont.DIGITAL_KHATT,
                onClick = { selectedFont = MushafFont.DIGITAL_KHATT }
            )
            Text(text = "Digital Khatt")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.changeFont(selectedFont)
                onApply()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apply")
        }
    }
}