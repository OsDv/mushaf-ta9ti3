package com.example.mushaf_ta9ti3.dao

import androidx.room3.Dao
import androidx.room3.Query
import com.example.mushaf_ta9ti3.model.QuranWord

@Dao
interface WordDao {
    @Query("SELECT * FROM words")
    suspend fun getAllWords(): List<QuranWord>

    @Query("SELECT * FROM words WHERE id BETWEEN :startId AND :endId")
    suspend fun getWordsInRange(startId: Int, endId: Int): List<QuranWord>

    @Query("SELECT w.id FROM words w WHERE w.surah = :surah AND w.word=1")
    suspend fun getAyahsStartBySurah(surah : Int): List<Int>
    @Query("SELECT w.* FROM words w WHERE w.surah = :surah AND w.word=1")
    suspend fun getAyahsStartBySurahWords(surah : Int): List<QuranWord>

    @Query("SELECT w.id FROM words w WHERE w.surah = :surah AND w.ayah = :ayah ORDER BY w.id ASC")
    suspend fun getWordsByAyah(surah :Int, ayah : Int): List<Int>

    @Query("SELECT * from words where id = :id")
    suspend fun getWordById(id : Int): QuranWord
}