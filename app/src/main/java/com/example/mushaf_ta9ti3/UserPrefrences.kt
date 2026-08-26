package com.example.mushaf_ta9ti3
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mushaf_ta9ti3.enum.MushafFont
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")
class UserPreferences(private val context: Context) {
    private val FONT_KEY = stringPreferencesKey("mushaf_font")
    val currentFontFlow: Flow<MushafFont> = context.dataStore.data.map { preferences ->
        val savedFontName = preferences[FONT_KEY] ?: MushafFont.STANDARD.name
        try {
            MushafFont.valueOf(savedFontName)
        } catch (e: Exception) {
            MushafFont.STANDARD
        }
    }
    suspend fun saveFont(font: MushafFont) {
        context.dataStore.edit { preferences ->
            preferences[FONT_KEY] = font.name
        }
    }
}