package com.example.mushaf_ta9ti3.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "words")
data class QuranWord (
    @PrimaryKey val id :Int,
    val location :String,
    val surah :Int,
    val ayah :Int,
    val word :Int,
    val text :String
)
