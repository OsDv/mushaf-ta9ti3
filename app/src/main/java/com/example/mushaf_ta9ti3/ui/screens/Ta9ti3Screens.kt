package com.example.mushaf_ta9ti3.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mushaf_ta9ti3.R
import com.example.mushaf_ta9ti3.enum.SelectionMode
import com.example.mushaf_ta9ti3.view.Ta9ti3ViewModel


@Composable
fun Ta9ti3SetupScreen(onStartTest: () -> Unit, onBack: () -> Boolean, viewModel: Ta9ti3ViewModel) {
    val surahs by viewModel.surahList.collectAsState()
    val hizbs by viewModel.hizbList.collectAsState()

    val surahNames = surahs.map {"${it.id}. ${it.nameArabic}"}
    val hizbNames = hizbs.map {"${it.hizbNumber} ${stringResource(R.string.alhizb)}"}
    val selectedSurahs by viewModel.selectedSurahs.collectAsState()
    val selectedHizbs by viewModel.selectedHizbs.collectAsState()

//    val primary = MaterialTheme.colorScheme.primary
    val defaultTextColor = MaterialTheme.colorScheme.onBackground
    val selectedColor = MaterialTheme.colorScheme.primary

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        HizbSurahSelectionScreen(
            surahList = surahNames,
            hizbList = hizbNames,
            onSurahClick = { index -> viewModel.toggleSurahSelection(index + 1) },
            onHizbClick = { index -> viewModel.toggleHizbSelection(index + 1) },
            getTextColor = { type, index ->
                when (type) {
                    SelectionMode.SURAH -> if (selectedSurahs.contains(index + 1)) selectedColor else defaultTextColor
                    SelectionMode.HIZB -> if (selectedHizbs.contains(index + 1)) selectedColor else defaultTextColor
                }
            },
            onTabChange = { tab -> viewModel.setSelectionMode(tab) },
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = { viewModel.initTest(onStartTest) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

        )
        {Text(text = stringResource(R.string.startTest)) }
    }
}
@Composable
fun Ta9ti3SessionScreen(onEndSession: () -> Unit, viewModel: Ta9ti3ViewModel)
{
    var swipeOffsetX by remember { mutableFloatStateOf(0f) }
    val correctAnswers by viewModel.correctlyAnswered.collectAsState()
    val incorrectAnswers by viewModel.incorrectlyAnswered.collectAsState()
    val totalQuestions = correctAnswers.size + incorrectAnswers.size
    val correctAnswerNum = correctAnswers.size
    Column(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragEnd = {
                    if (swipeOffsetX > 400f) {
                        viewModel.goToNextPage()
                    } else if (swipeOffsetX < -400f) {
                        viewModel.goToPreviousPage()
                    }
                    swipeOffsetX = 0f
                },
                onDragCancel = {
                    swipeOffsetX = 0f
                },
                onHorizontalDrag = { change, dragAmount ->
                    change.consume()
                    swipeOffsetX += dragAmount
                }
            )
        }
    ) {
        val lines by viewModel.currentPageLines.collectAsState()
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text(text = "${stringResource(R.string.theQuestion)} $totalQuestions")
            Text(text = "${stringResource(R.string.correctAnswers)} $correctAnswerNum", color = Color.Green)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 16.dp, start = 12.dp, end = 12.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            val textColor = MaterialTheme.colorScheme.onBackground
            val currentCursor = viewModel.CurrentPointer.collectAsState()
            Mushaf(
                viewModel = viewModel,
                lines = lines,
                textColor = {id->
                if (id <= currentCursor.value) {
                    textColor
                } else {
                    Color.Transparent
                }
            })
        }
        Row()
        {
            val correctButtonColors = ButtonColors(
                containerColor = Color.Green,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
            val incorrectButtonColors = ButtonColors(
                containerColor = Color.Red,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
            Button(onClick = { viewModel.onCorrectAnswer() }, colors = correctButtonColors){Text(text = stringResource(R.string.correctAnswers))}
            Button(onClick = { viewModel.onWrongAnswer() }, colors = incorrectButtonColors){Text(text = stringResource(R.string.wrongAnswers))}
            Button(onClick = { viewModel.onEndSession(onEndSession = onEndSession) }){Text(text = stringResource(R.string.endExam))}
        }
        Row(horizontalArrangement = Arrangement.Center)
        {
            Button(onClick = {viewModel.onShowWord()}, modifier = Modifier.weight(1f)){Text(text = stringResource(R.string.showWord))}
            Button(onClick = {viewModel.onShowAyah()}, modifier = Modifier.weight(1f)){Text(text = stringResource(R.string.showAyah))}
        }
    }
}

@Composable
fun Ta9ti3ResultScreen(onReturnHome: () -> Unit, viewModel: Ta9ti3ViewModel)
{
    val correctlyAnswered = viewModel.correctlyAnswered
    val incorrectlyAnswered = viewModel.incorrectlyAnswered
    val correctAnswersCount = correctlyAnswered.collectAsState().value.size
    val incorrectAnswersCount = incorrectlyAnswered.collectAsState().value.size
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "${stringResource(R.string.correctAnswers)} $correctAnswersCount")
        Text(text = "${stringResource(R.string.wrongAnswers)} $incorrectAnswersCount")
        Button(onClick = { onReturnHome() }){Text(text = stringResource(R.string.back))}
    }
}




@Composable
fun HizbSurahSelectionScreen(
    surahList: List<String>,
    hizbList: List<String>,
    onSurahClick: (index: Int) -> Unit,
    onHizbClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    getTextColor: (type: SelectionMode, index: Int) -> Color,
    onTabChange: (SelectionMode) -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableStateOf(SelectionMode.SURAH) }
    Column(modifier = modifier.fillMaxSize()) {
        // Top switch buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    selectedTab = SelectionMode.SURAH
                    onTabChange(SelectionMode.SURAH)
                },
                modifier = Modifier.weight(1f),
                colors = if (selectedTab == SelectionMode.SURAH) {
                    ButtonDefaults.buttonColors()
                } else {
                    ButtonDefaults.outlinedButtonColors()
                }
            ) {
                Text(text = stringResource(R.string.surah))
            }

            Button(
                onClick = {
                    selectedTab = SelectionMode.HIZB
                    onTabChange(SelectionMode.HIZB)
                },
                modifier = Modifier.weight(1f),
                colors = if (selectedTab == SelectionMode.HIZB) {
                    ButtonDefaults.buttonColors()
                } else {
                    ButtonDefaults.outlinedButtonColors()
                }
            ) {
                Text(text = stringResource(R.string.alhizb))
            }
        }
        when (selectedTab) {
            SelectionMode.SURAH -> SelectableList(
                items = surahList,
                onItemClick = onSurahClick,
                getTextColor = getTextColor,
                type = SelectionMode.SURAH,
                modifier = Modifier.weight(1f)
            )
            SelectionMode.HIZB -> SelectableList(
                items = hizbList,
                onItemClick = onHizbClick,
                getTextColor = getTextColor,
                type = SelectionMode.HIZB,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


//@Composable
//private fun SelectableList(
//    items: List<String>,
//    onItemClick: (index: Int) -> Unit,
//    modifier: Modifier = Modifier,
//    getTextColor: (type: SelectionMode, index: Int) -> Color,
//    type : SelectionMode = SelectionMode.SURAH
//) {
//    LazyColumn(modifier = modifier.fillMaxSize()) {
//        itemsIndexed(items) { index, item ->
//            Text(
//                text = item,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { onItemClick(index) }
//                    .padding(16.dp),
//                color = getTextColor(type, index)
//            )
//        }
//    }
//}
@Composable
private fun SelectableList(
    items: List<String>,
    onItemClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    getTextColor: (type: SelectionMode, index: Int) -> Color,
    type: SelectionMode = SelectionMode.SURAH
) {
    LazyColumn(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {
        itemsIndexed(items) { index, item ->

            val textColor = getTextColor(type, index)
            val isSelected = textColor == MaterialTheme.colorScheme.primary

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onItemClick(index) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(16.dp),
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 18.sp
                )
            }
        }
    }
}