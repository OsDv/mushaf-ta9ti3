package com.example.mushaf_ta9ti3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mushaf_ta9ti3.database.QuranDatabase
import com.example.mushaf_ta9ti3.repository.QuranRepository
import com.example.mushaf_ta9ti3.ui.theme.Mushafta9ti3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quranDB = QuranDatabase.getDatabase(applicationContext)
        val userPreferences = UserPreferences(applicationContext)

        val repository = QuranRepository(quranDB = quranDB,userPreferences = userPreferences)

        val factory = AppViewModelFactory(repository,userPreferences)

        setContent {
            Mushafta9ti3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(viewModelFactory = factory)
                    }

                }
            }
        }
    }
}