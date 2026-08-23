package com.example.mushaf_ta9ti3.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.mushaf_ta9ti3.model.Chapter
import com.example.mushaf_ta9ti3.model.Hizb
import com.example.mushaf_ta9ti3.model.Page
import com.example.mushaf_ta9ti3.model.QuranWord
import com.example.mushaf_ta9ti3.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Collections

open class MushafViewModel(private val repository: QuranRepository) : ViewModel() {
    private val _pageList = MutableStateFlow<List<Page>>(emptyList())
    val pageList: StateFlow<List<Page>> = _pageList.asStateFlow()

    private val _hizbList = MutableStateFlow<List<Hizb>>(Collections.emptyList())
    val hizbList: StateFlow<List<Hizb>> = _hizbList.asStateFlow()
    private val _surahList = MutableStateFlow<List<Chapter>>(Collections.emptyList())
    val surahList: StateFlow<List<Chapter>> = _surahList.asStateFlow()

    // private val _wordList = MutableStateFlow<List<QuranWord>>(emptyList())
    // val wordList: StateFlow<List<QuranWord>> = _wordList.asStateFlow()

    private val _currentPage = MutableStateFlow<Int?>(null)
    val currentPage: StateFlow<Int?> = _currentPage.asStateFlow()

    private val _currentPageLines = MutableStateFlow<List<List<QuranWord>>>(emptyList())
    val currentPageLines: StateFlow<List<List<QuranWord>>> = _currentPageLines.asStateFlow()

    private val surahsStarts = mutableListOf<Int>()
    val surahStartsList: List<Int> get() = surahsStarts

    private var basmalah : String = ""
    val basmalahText: String get() = basmalah


    private var _pagesNumber = 0
    private var _linesPerPage = 0


    init {
        viewModelScope.launch {
            _pageList.value = repository.getAllPages()
            _hizbList.value = repository.getAllHizbs()
            _surahList.value = repository.getAllChapters()
            basmalah = repository.getBasmalah()
            // _wordList.value = repository.getAllWords()
            _currentPage.value = 1
            _pagesNumber = repository.getNumberOfPages()
            _linesPerPage = repository.getLinesPerPage()
            surahsStarts.addAll(repository.getSurahsStarts())
            loadPage(1)
        }

    }
    fun goToNextPage() {
        val current = _currentPage.value ?: return
        if (current < _pagesNumber) {
            loadPage(current + 1)
            _currentPage.value = current + 1
        }
    }

    fun goToPreviousPage() {
        val current = _currentPage.value ?: return
        if (current > 1) {
            loadPage(current - 1)
            _currentPage.value = current - 1
        }
    }
    fun loadPage(pageNumber: Int) {
        // 1. Launch a coroutine tied to the ViewModel
        viewModelScope.launch {

            _currentPageLines.value = emptyList()

            val pages = _pageList.value.filter { it.page_number == pageNumber }

            val lines = mutableListOf<List<QuranWord>>()

            pages.forEach { page ->
                when (page.line_type) {
                    "ayah" -> {
                        val words = repository.getWordsInRange(page.first_word_id, page.last_word_id)
                        lines.add(words)
                    }
                    // (handling for "surah_name" or "basmalah" here later)
                }
            }
            _currentPageLines.value = lines
            _currentPage.value = pageNumber
        }
    }
    fun navigateToSurah(surahId: Int) {
        val start = surahsStarts[surahId - 1]
        val startPage = _pageList.value.find { it.first_word_id <= start && start <= it.last_word_id }
        loadPage(startPage?.page_number ?: 1)
    }
    fun navigateToHizb(hizbId: Int) {
        val hizb = _hizbList.value.find { it.hizbNumber == hizbId }
        val surah = hizb?.firstVerseKey?.split(":")[0]?.toInt()?:1
        val ayah = hizb?.firstVerseKey?.split(":")[1]?.toInt()?:1
        viewModelScope.launch {
            val words = repository.getWordsByAyah(surah, ayah)
            val start = words.first()
            val startPage = _pageList.value.find { it.first_word_id <= start && start <= it.last_word_id }
            loadPage(startPage?.page_number ?: 1)
        }
    }
}