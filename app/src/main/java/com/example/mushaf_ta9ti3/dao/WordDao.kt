package com.example.mushaf_ta9ti3.dao

import androidx.room3.Dao
import androidx.room3.RawQuery
import androidx.room3.RoomRawQuery
import com.example.mushaf_ta9ti3.model.QuranWord

@Dao
interface WordDao {
    @RawQuery
    suspend fun getAllWords(query: RoomRawQuery): List<QuranWord>

    @RawQuery
    suspend fun getWordsInRange(query: RoomRawQuery): List<QuranWord>

    @RawQuery
    suspend fun getAyahsStartBySurah(query: RoomRawQuery): List<Int>

    @RawQuery
    suspend fun getAyahsStartBySurahWords(query: RoomRawQuery): List<QuranWord>

    @RawQuery
    suspend fun getWordsByAyah(query: RoomRawQuery): List<Int>

    @RawQuery
    suspend fun getWordById(query: RoomRawQuery): QuranWord

    @RawQuery
    suspend fun getSurahsStart(query: RoomRawQuery): List<Int>

    @RawQuery
    suspend fun getBasmalah(query: RoomRawQuery): List<String>

    @RawQuery
    suspend fun getAyahsStartBySurahs(query: RoomRawQuery): List<Int>
}