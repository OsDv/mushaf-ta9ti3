package com.example.mushaf_ta9ti3.dao

import androidx.room3.Dao
import androidx.room3.Query
import com.example.mushaf_ta9ti3.model.Hizb

@Dao
interface HizbDao {
    // Get all 60 Hizbs to populate your selection menu
    @Query("SELECT * FROM hizbs ORDER BY hizb_number ASC")
    suspend fun getAllHizbs(): List<Hizb>

    // Get the exact details of a specific Hizb when the user selects it
    @Query("SELECT * FROM hizbs WHERE hizb_number = :targetHizb")
    suspend fun getHizbByNumber(targetHizb: Int): Hizb
}