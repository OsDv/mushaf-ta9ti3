package com.example.mushaf_ta9ti3.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mushaf_ta9ti3.R

@Composable
fun HomeScreen(onNavigateToMushaf: () -> Unit, onNavigateToTa9ti3: () -> Unit,onNavigationToSettings: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF1E9C5)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(painter = painterResource(R.drawable.ta9ti3_logo), contentDescription = null)
        Button(onClick = onNavigateToMushaf, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = stringResource(R.string.mushaf))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToTa9ti3, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = stringResource(R.string.ta9ti3))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigationToSettings, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = stringResource(R.string.settings))
        }
    }
}