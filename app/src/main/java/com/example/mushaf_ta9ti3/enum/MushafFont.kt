package com.example.mushaf_ta9ti3.enum

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.mushaf_ta9ti3.R

val MushafFontFamily = FontFamily(
    Font(R.font.quran_font, FontWeight.Normal)
)
val DigitalKhatFontFamily = FontFamily(
    Font(R.font.digital_khat, FontWeight.Normal)
)
val SurahNameFontFamily = FontFamily(
    Font(R.font.surah_name, FontWeight.Normal)
)

enum class MushafFont(val tableName: String, val fontFamily: FontFamily) {
    STANDARD("words", MushafFontFamily),
    DIGITAL_KHATT("words_v2", DigitalKhatFontFamily)
}