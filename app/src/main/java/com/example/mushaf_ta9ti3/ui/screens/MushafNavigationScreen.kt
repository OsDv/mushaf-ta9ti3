package com.example.mushaf_ta9ti3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.mushaf_ta9ti3.view.MushafViewModel

@Composable
fun MushafNavigationScreen(
    viewModel: MushafViewModel,
    onSelectionMade: () -> Unit,
    onBack: () -> Boolean
) {
    onSelectionMade()
}