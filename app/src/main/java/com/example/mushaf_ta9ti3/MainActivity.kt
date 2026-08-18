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

        // 2. Create the Repository instance
        val repository = QuranRepository(quranDB = quranDB)

        // 3. Create the Factory instance
        val factory = AppViewModelFactory(repository)

        setContent {
            Mushafta9ti3Theme {
                // Scaffold automatically calculates the exact size of the status bar and phone buttons
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // We apply that padding to a Box, and put your AppNavigation safely inside it
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(viewModelFactory = factory)
                    }

                }
            }
        }
    }
}