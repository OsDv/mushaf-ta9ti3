package com.example.mushaf_ta9ti3.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "hizbs")
data class Hizb(
    @PrimaryKey
    @ColumnInfo(name = "hizb_number")
    val hizbNumber: Int,

    @ColumnInfo(name = "verses_count")
    val versesCount: Int,

    @ColumnInfo(name = "first_verse_key")
    val firstVerseKey: String,

    @ColumnInfo(name = "last_verse_key")
    val lastVerseKey: String,

    @ColumnInfo(name = "verse_mapping")
    val verseMapping: String // JSON
)