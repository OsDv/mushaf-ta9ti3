package com.example.mushaf_ta9ti3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.mushaf_ta9ti3.R
import com.example.mushaf_ta9ti3.ui.theme.Mushafta9ti3Theme
import com.example.mushaf_ta9ti3.view.MushafViewModel

@Composable
fun MushafNavigationScreen(
    viewModel: MushafViewModel,
    onSelectionMade: () -> Unit,
    onBack: () -> Boolean
) {
    val textColors = MaterialTheme.colorScheme.onBackground
    val surahs by viewModel.surahList.collectAsState()
    val hizbs by viewModel.hizbList.collectAsState()

    val surahNames = surahs.map {"${it.id}. ${it.nameArabic}"}
    val hizbNames = hizbs.map {"${it.hizbNumber} ${stringResource(R.string.alhizb)}"}
    HizbSurahSelectionScreen(
        surahList = surahNames,
        hizbList = hizbNames,
        onSurahClick = { index -> onSurahClick(surahId = index + 1, viewModel = viewModel, onSelectionMade = onSelectionMade) },
        onHizbClick = { index -> onHizbClick(hizbId = index + 1, viewModel = viewModel, onSelectionMade = onSelectionMade) },
        modifier = Modifier,
        getTextColor = { type, index -> textColors },
        onTabChange = {  }
    )
}

fun onSurahClick(surahId: Int, viewModel: MushafViewModel, onSelectionMade: () -> Unit) {
    viewModel.navigateToSurah(surahId)
    onSelectionMade()
}
fun onHizbClick(hizbId: Int, viewModel: MushafViewModel, onSelectionMade: () -> Unit) {
    viewModel.navigateToHizb(hizbId)
    onSelectionMade()
}