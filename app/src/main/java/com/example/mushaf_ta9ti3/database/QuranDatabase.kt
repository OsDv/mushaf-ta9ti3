package com.example.mushaf_ta9ti3.database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.mushaf_ta9ti3.dao.PageDao
import com.example.mushaf_ta9ti3.dao.WordDao
import com.example.mushaf_ta9ti3.dao.ChapterDao
import com.example.mushaf_ta9ti3.dao.HizbDao
import com.example.mushaf_ta9ti3.model.Page
import com.example.mushaf_ta9ti3.model.LayoutInfo
import com.example.mushaf_ta9ti3.model.QuranWord
import com.example.mushaf_ta9ti3.model.Chapter
import com.example.mushaf_ta9ti3.model.Hizb

@Database(
    entities = [Page::class, LayoutInfo::class, QuranWord::class, Chapter::class, Hizb::class],
    version = 1,
    exportSchema = false
)
abstract class QuranDatabase : RoomDatabase() {

    abstract fun pageDao(): PageDao
    abstract fun wordDao(): WordDao
    abstract fun chapterDao(): ChapterDao
    abstract fun hizbDao(): HizbDao

    companion object {
        @Volatile
        private var INSTANCE: QuranDatabase? = null

        fun getDatabase(context: Context): QuranDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuranDatabase::class.java,
                    "quran.db"
                )
                    .createFromAsset("quran.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}