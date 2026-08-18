package com.example.mushaf_ta9ti3.dao

import androidx.room3.Dao
import androidx.room3.Query
import com.example.mushaf_ta9ti3.model.Page

@Dao
interface PageDao {
    @Query("SELECT * FROM pages")
    suspend fun getAllPages(): List<Page> // Added suspend

    @Query("SELECT * FROM pages as p WHERE p.page_number= :pageNumber")
    suspend fun getPageByNumber(pageNumber :Int): Page // Added suspend

    @Query("SELECT number_of_pages from info")
    suspend fun getNumberOfPages(): Int

    @Query("SELECT lines_per_page from info")
    suspend fun getLinesPerPage(): Int

    @Query("SELECT page_number FROM pages WHERE :word BETWEEN first_word_id AND last_word_id")
    suspend fun getWordPage(word : Int): Int
}