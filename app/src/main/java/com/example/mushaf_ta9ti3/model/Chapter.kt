package com.example.mushaf_ta9ti3.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "chapters")
data class Chapter(
    @PrimaryKey
    val id: Int,

    val name: String,

    @ColumnInfo(name = "name_simple")
    val nameSimple: String,

    @ColumnInfo(name = "name_arabic")
    val nameArabic: String,

    @ColumnInfo(name = "revelation_order")
    val revelationOrder: Int,

    @ColumnInfo(name = "revelation_place")
    val revelationPlace: String,

    @ColumnInfo(name = "verses_count")
    val versesCount: Int,

    // Kept as Int since SQLite uses 1/0 for true/false
    @ColumnInfo(name = "bismillah_pre")
    val bismillahPre: Int
)