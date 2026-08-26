package com.example.mushaf_ta9ti3.repository
import androidx.room3.RoomRawQuery
import com.example.mushaf_ta9ti3.UserPreferences
import com.example.mushaf_ta9ti3.dao.ChapterDao
import com.example.mushaf_ta9ti3.dao.HizbDao
import com.example.mushaf_ta9ti3.dao.PageDao
import com.example.mushaf_ta9ti3.dao.WordDao
import com.example.mushaf_ta9ti3.database.QuranDatabase
import com.example.mushaf_ta9ti3.model.Chapter
import com.example.mushaf_ta9ti3.model.Hizb
import com.example.mushaf_ta9ti3.model.Page
import com.example.mushaf_ta9ti3.model.QuranWord
import kotlinx.coroutines.flow.first

class QuranRepository(quranDB: QuranDatabase,private val userPreferences: UserPreferences) {
    val pageDao: PageDao = quranDB.pageDao()
    val wordDao: WordDao = quranDB.wordDao()
    val chapterDao: ChapterDao = quranDB.chapterDao()
    val hizbDao: HizbDao = quranDB.hizbDao()
    suspend fun getAllPages(): List<Page> {
        return pageDao.getAllPages()
    }
    suspend fun getAllWords(): List<QuranWord> {
        val query = RoomRawQuery(sql = "SELECT * FROM ${getWordsTableName()}")
        return wordDao.getAllWords(query)
    }
    suspend fun getPageByNumber(pageNumber :Int): Page {
        return pageDao.getPageByNumber(pageNumber)
    }
    suspend fun getNumberOfPages(): Int {
        return pageDao.getNumberOfPages()
    }
    suspend fun getLinesPerPage(): Int {
        return pageDao.getLinesPerPage()
    }
    suspend fun getWordsInRange(startId: Int, endId: Int): List<QuranWord> {
        val query = RoomRawQuery(
            sql = "SELECT * FROM ${getWordsTableName()} WHERE id BETWEEN ? AND ?",
            onBindStatement = { statement ->
                statement.bindLong(1, startId.toLong())
                statement.bindLong(2, endId.toLong())
            }
        )
        return wordDao.getWordsInRange(query)
    }
    suspend fun getChapterById(chapterId: Int): Chapter {
        return chapterDao.getChapterById(chapterId)
    }
    suspend fun getHizbByNumber(hizbNumber: Int): Hizb {
        return hizbDao.getHizbByNumber(hizbNumber)
    }
    suspend fun getAllChapters(): List<Chapter> {
        return chapterDao.getAllChapters()
    }
    suspend fun getAllHizbs(): List<Hizb> {
        return hizbDao.getAllHizbs()
    }

    suspend fun getAyahsStartBySurah(surah: Int): List<Int> {
        val query = RoomRawQuery(
            sql = "SELECT id FROM ${getWordsTableName()} WHERE surah = ? AND word = 1",
            onBindStatement = { statement ->
                statement.bindLong(1, surah.toLong())
            }
        )
        return wordDao.getAyahsStartBySurah(query)
    }

    suspend fun getAyahsStartBySurahs(surahs: List<Int>): List<Int> {
        if (surahs.isEmpty()) return emptyList()
        val placeholders = surahs.joinToString(",") { "?" }

        val query = RoomRawQuery(
            sql = "SELECT id FROM ${getWordsTableName()} WHERE surah IN ($placeholders) AND word = 1",
            onBindStatement = { statement ->
                surahs.forEachIndexed { index, surahId ->
                    statement.bindLong(index + 1, surahId.toLong())
                }
            }
        )
        return wordDao.getAyahsStartBySurahs(query)
    }

    suspend fun getAyahsStartByHizbs(hizbNumbers: List<Int>): List<Int> {
        val result = mutableListOf<Int>()
        for (hizbNumber in hizbNumbers) {
            val hizb = hizbDao.getHizbByNumber(hizbNumber)

            val (startSurahStr, startAyahStr) = hizb.firstVerseKey.split(":")
            val (endSurahStr, endAyahStr) = hizb.lastVerseKey.split(":")

            val startSurah = startSurahStr.toInt()
            val startAyah = startAyahStr.toInt()
            val endSurah = endSurahStr.toInt()
            val endAyah = endAyahStr.toInt()

            for (surah in startSurah..endSurah) {
                val newWords = getAyahsStartBySurahWords(surah)

                // Fixed filter logic to prevent accidentally grabbing the whole Surah
                val filteredIds = newWords.filter { word ->
                    when {
                        startSurah == endSurah -> word.ayah in startAyah..endAyah
                        word.surah == startSurah -> word.ayah >= startAyah
                        word.surah == endSurah -> word.ayah <= endAyah
                        else -> true
                    }
                }.map { it.id }

                result.addAll(filteredIds)
            }
        }
        return result
    }

    suspend fun getAyahsStartBySurahWords(surah: Int): List<QuranWord> {
        val query = RoomRawQuery(
            sql = "SELECT * FROM ${getWordsTableName()} WHERE surah = ? AND word = 1",
            onBindStatement = { statement ->
                statement.bindLong(1, surah.toLong())
            }
        )
        return wordDao.getAyahsStartBySurahWords(query)
    }

    suspend fun getWordsByAyah(surah: Int, ayah: Int): List<Int> {
        val query = RoomRawQuery(
            sql = "SELECT id FROM ${getWordsTableName()} WHERE surah = ? AND ayah = ? ORDER BY id ASC",
            onBindStatement = { statement ->
                statement.bindLong(1, surah.toLong())
                statement.bindLong(2, ayah.toLong())
            }
        )
        return wordDao.getWordsByAyah(query)
    }

    suspend fun getWordById(id: Int): QuranWord {
        val query = RoomRawQuery(
            sql = "SELECT * FROM ${getWordsTableName()} WHERE id = ?",
            onBindStatement = { statement ->
                statement.bindLong(1, id.toLong())
            }
        )
        return wordDao.getWordById(query)
    }

    suspend fun getWordPage(word: Int): Int {
        return pageDao.getWordPage(word)
    }

    suspend fun getSurahsStarts(): List<Int> {
        val query = RoomRawQuery(
            sql = "SELECT id FROM ${getWordsTableName()} WHERE ayah = 1 AND word = 1"
        )
        return wordDao.getSurahsStart(query)
    }

    suspend fun getBasmalah(): List<String> {
        val query = RoomRawQuery(
            sql = "SELECT text FROM ${getWordsTableName()} WHERE id BETWEEN 1 AND 4"
        )
        return wordDao.getBasmalah(query)
    }
    private suspend fun getWordsTableName(): String {
        return userPreferences.currentFontFlow.first().tableName
    }
}