package com.example.mushaf_ta9ti3.dao

import androidx.room3.Dao
import androidx.room3.Query
import com.example.mushaf_ta9ti3.model.Chapter

@Dao
interface ChapterDao {
    // Get all chapters (useful for a Surah list menu)
    @Query("SELECT * FROM chapters ORDER BY id ASC")
    suspend fun getAllChapters(): List<Chapter>

    // Get a specific chapter by its ID
    @Query("SELECT * FROM chapters WHERE id = :chapterId")
    suspend fun getChapterById(chapterId: Int): Chapter
}