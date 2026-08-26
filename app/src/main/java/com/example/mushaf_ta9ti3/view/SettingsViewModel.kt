package com.example.mushaf_ta9ti3.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mushaf_ta9ti3.UserPreferences
import com.example.mushaf_ta9ti3.enum.MushafFont
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val currentFont: StateFlow<MushafFont> = userPreferences.currentFontFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MushafFont.STANDARD
        )

    fun changeFont(newFont: MushafFont) {
        viewModelScope.launch {
            userPreferences.saveFont(newFont)
        }
    }
}