package com.example.mushaf_ta9ti3.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "info")
data class LayoutInfo(
    @PrimaryKey
    val name: String,
    val number_of_pages: Int,
    val lines_per_page: Int,
    val font_name: String
)