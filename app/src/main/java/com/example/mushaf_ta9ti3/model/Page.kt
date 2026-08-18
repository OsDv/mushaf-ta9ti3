package com.example.mushaf_ta9ti3.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "pages", primaryKeys = ["page_number", "line_number"])
data class Page (
    val page_number :Int,
    val line_number :Int,
    val line_type :String,
    val is_centered : Int,
    val first_word_id :Int,
    val last_word_id :Int,
    val surah_number :Int
)