package com.example.mushaf_ta9ti3.ui.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mushaf_ta9ti3.R

@Composable
fun HomeScreen(onNavigateToMushaf: () -> Unit, onNavigateToTa9ti3: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onNavigateToMushaf, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = stringResource(R.string.mushaf))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToTa9ti3, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = stringResource(R.string.ta9ti3))
        }
    }
}