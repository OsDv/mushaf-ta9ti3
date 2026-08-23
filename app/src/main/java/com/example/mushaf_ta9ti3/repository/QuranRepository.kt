package com.example.mushaf_ta9ti3.repository
import com.example.mushaf_ta9ti3.dao.ChapterDao
import com.example.mushaf_ta9ti3.dao.HizbDao
import com.example.mushaf_ta9ti3.dao.PageDao
import com.example.mushaf_ta9ti3.dao.WordDao
import com.example.mushaf_ta9ti3.database.QuranDatabase
import com.example.mushaf_ta9ti3.model.Chapter
import com.example.mushaf_ta9ti3.model.Hizb
import com.example.mushaf_ta9ti3.model.Page
import com.example.mushaf_ta9ti3.model.QuranWord

class QuranRepository(quranDB: QuranDatabase) {
    val pageDao: PageDao = quranDB.pageDao()
    val wordDao: WordDao = quranDB.wordDao()
    val chapterDao: ChapterDao = quranDB.chapterDao()
    val hizbDao: HizbDao = quranDB.hizbDao()
    suspend fun getAllPages(): List<Page> {
        return pageDao.getAllPages()
    }
    suspend fun getAllWords(): List<QuranWord> {
        return wordDao.getAllWords()
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
        return wordDao.getWordsInRange(startId, endId)
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

    suspend fun getAyahsStartBySurahs(surahs: List<Int>): List<Int> {
        val result = mutableListOf<Int>()
        surahs.forEach { surah ->  result.addAll(wordDao.getAyahsStartBySurah(surah))}
        return result
    }
    suspend fun getAyahsStartByHizbs(hizbNumbers: List<Int>): List<Int> {
        val result = mutableListOf<Int>()
        val hizbs = mutableListOf<Hizb>()
        hizbNumbers.forEach { hizbNumber ->  hizbs.add(hizbDao.getHizbByNumber(hizbNumber))}
        hizbs.forEach { hizb ->
            val startSurah = hizb.firstVerseKey.split(":")[0].toInt()
            val startAyah = hizb.firstVerseKey.split(":")[1].toInt()
            val endSurah = hizb.lastVerseKey.split(":")[0].toInt()
            val endAyah = hizb.lastVerseKey.split(":")[1].toInt()
            for (surah in startSurah..endSurah) {
                val newWords = wordDao.getAyahsStartBySurahWords(surah)
                newWords.filter { (it.surah != startSurah && it.surah!=endSurah)||
                        (it.surah== startSurah && it.ayah>=startAyah)||
                        (it.surah==endSurah && it.ayah<=endAyah)}.forEach { result.add(it.id) }
            }
        }
        return result
    }
    suspend fun getWordsByAyah(surah:Int, ayah: Int): List<Int> {
        return wordDao.getWordsByAyah(surah, ayah)
    }
    suspend fun getWordById(id: Int): QuranWord {
        return wordDao.getWordById(id)
    }
    suspend fun getWordPage(word: Int): Int {
        return pageDao.getWordPage(word)
    }
    suspend fun getSurahsStarts(): List<Int> {
        return wordDao.getSurahsStart()
    }
    suspend fun getBasmalah(): String {
        return wordDao.getBasmalah().joinToString(" ")
    }
}