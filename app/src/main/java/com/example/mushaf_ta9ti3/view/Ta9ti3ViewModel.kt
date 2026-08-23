package com.example.mushaf_ta9ti3.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mushaf_ta9ti3.enum.SelectionMode
import com.example.mushaf_ta9ti3.model.Chapter
import com.example.mushaf_ta9ti3.model.Hizb
import com.example.mushaf_ta9ti3.model.QuranWord
import com.example.mushaf_ta9ti3.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Collections.emptyList


class Ta9ti3ViewModel(private val repository: QuranRepository) : MushafViewModel(repository) {


    private val _selectedHizbs = MutableStateFlow<Set<Int>>(emptySet())
    val selectedHizbs: StateFlow<Set<Int>> = _selectedHizbs.asStateFlow()

    private val _selectedSurahs = MutableStateFlow<Set<Int>>(emptySet())
    val selectedSurahs: StateFlow<Set<Int>> = _selectedSurahs.asStateFlow()

    private var _selectionMode = SelectionMode.SURAH

    private var testWords : List<Int> = emptyList()
    private val _currentPointer = MutableStateFlow<Int>(0)
    val CurrentPointer: StateFlow<Int> = _currentPointer.asStateFlow()


    private val _correctlyAnswered = MutableStateFlow<MutableList<List<QuranWord>>>(mutableListOf())
    val correctlyAnswered = _correctlyAnswered.asStateFlow()

    private val _incorrectlyAnswered = MutableStateFlow<MutableList<List<QuranWord>>>(mutableListOf())
    val incorrectlyAnswered = _incorrectlyAnswered.asStateFlow()

    private var currentQuestionAyah : List<QuranWord> = emptyList()

    fun setSelectionMode(mode: SelectionMode) {
        _selectionMode = mode
    }

    init {
        viewModelScope.launch {
        }
    }
    fun resetSelectedHizbs() {
        _selectedHizbs.value = emptySet()
    }

    fun resetSelectedSurahs() {
        _selectedSurahs.value = emptySet()
    }
    fun toggleHizbSelection(hizbNumber: Int) {
        val currentSet = _selectedHizbs.value.toMutableSet()
        if (currentSet.contains(hizbNumber)) {
            currentSet.remove(hizbNumber)
        } else {
            currentSet.add(hizbNumber)
        }
        _selectedHizbs.value = currentSet
    }

    fun toggleSurahSelection(chapterId: Int) {
        val currentSet = _selectedSurahs.value.toMutableSet()
        if (currentSet.contains(chapterId)) {
            currentSet.remove(chapterId)
        } else {
            currentSet.add(chapterId)
        }
        _selectedSurahs.value = currentSet
        Log.d("myLog", "surah toggled ID: $chapterId")
    }
    fun initTest(onStartTest : () -> Unit)
    {
        viewModelScope.launch {
            when(_selectionMode){
                SelectionMode.SURAH -> testWords = repository.getAyahsStartBySurahs(_selectedSurahs.value.toList())
                SelectionMode.HIZB -> testWords = repository.getAyahsStartByHizbs(_selectedHizbs.value.toList())
            }
            if(testWords.isNotEmpty()) onStartTest()
            _correctlyAnswered.value.clear()
            _incorrectlyAnswered.value.clear()
            nextQuestion()
            onStartTest()
        }
    }

    fun nextQuestion()
    {
        viewModelScope.launch {
            val start = repository.getWordById(testWords.random())
            val ayahWordsIds = repository.getWordsByAyah(start.surah, start.ayah)
            currentQuestionAyah = repository.getWordsInRange(start.id, ayahWordsIds.last())
            _currentPointer.value = ayahWordsIds.last()
            val currentPageN = repository.getWordPage(start.id)
            loadPage(currentPageN)
        }
    }
    fun onCorrectAnswer()
    {
        _correctlyAnswered.value.add(currentQuestionAyah)
        nextQuestion()
    }
    fun onWrongAnswer()
    {
        _incorrectlyAnswered.value.add(currentQuestionAyah)
        nextQuestion()
    }
    fun onShowWord()
    {
        _currentPointer.value++
    }
    fun onShowAyah()
    {
        viewModelScope.launch {
            val start = repository.getWordById(_currentPointer.value)
            var newPointer = repository.getWordsByAyah(start.surah, start.ayah).last()
            if (newPointer == _currentPointer.value)
            {
                val start = repository.getWordById(_currentPointer.value+1)
                newPointer = repository.getWordsByAyah(start.surah, start.ayah).last()
            }
            _currentPointer.value = newPointer
        }
    }
    fun onEndSession(onEndSession: () -> Unit)
    {
        resetSelectedHizbs()
        resetSelectedSurahs()
        _currentPointer.value = 0
        testWords = emptyList()
        onEndSession()
    }
}