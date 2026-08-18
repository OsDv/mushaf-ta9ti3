package com.example.mushaf_ta9ti3.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mushaf_ta9ti3.R
import com.example.mushaf_ta9ti3.model.QuranWord
import com.example.mushaf_ta9ti3.view.MushafViewModel

val MushafFontFamily = FontFamily(
    Font(R.font.quran_font, FontWeight.Normal)
)

@Composable
fun MushafScreen(viewModel: MushafViewModel, onReturnHome: () -> Unit) {
    BackHandler(enabled = true){
        onReturnHome()
    }
    val lines by viewModel.currentPageLines.collectAsState()
    var swipeOffsetX by remember { mutableFloatStateOf(0f) }
    Column(modifier = Modifier.fillMaxSize()
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

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 16.dp, start = 12.dp, end = 12.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            val primary = MaterialTheme.colorScheme.primary
            Mushaf(lines = lines, textColor = {primary})
            //MushafWebView(lines = lines)
        }

        // 2. BOTTOM: The buttons row
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp), // Adds breathing room around the buttons
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Button(
//                onClick = { viewModel.goToPreviousPage() },
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(end = 8.dp)
//            ) {
//                Text("Previous Page")
//            }
//            Button(
//                onClick = { viewModel.goToNextPage() },
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(start = 8.dp)
//            ) {
//                Text("Next Page")
//            }
//        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Mushaf(
    lines: List<List<QuranWord>>,
    modifier: Modifier = Modifier,
    minRowHeight: Dp = 40.dp,
    textColor : (id :Int)-> Color
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // Landscape: ignore height constraint entirely, size by width only, scroll if needed
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            lines.forEach { line ->
                AutoSizeRow(
                    words = line.reversed(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                        textColor = textColor
                    // row height = whatever the chosen font needs
                    //constrainHeight = false
                )
            }
        }
    } else {
        BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            val availableHeight = maxHeight
            val neededHeight = minRowHeight * lines.size
            val contentHeight = maxOf(availableHeight, neededHeight)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(contentHeight)
                ) {
                    lines.forEach { line ->
                        AutoSizeRow(
                            words = line.reversed(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            textColor = textColor
                            //constrainHeight = true
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun AutoSizeRow(
    words: List<QuranWord>,
    modifier: Modifier = Modifier,
    constrainHeight: Boolean = true,
    minFontSize: TextUnit = 8.sp,
    maxFontSize: TextUnit = 60.sp,
    fontFamily: FontFamily = MushafFontFamily,
    textColor: (id: Int) -> Color
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val textMeasurer = rememberTextMeasurer()
        val maxWidthPx = with(density) { maxWidth.toPx() }
        // if height isn't constrained, effectively don't bound it
        val maxHeightPx = if (constrainHeight) {
            with(density) { maxHeight.toPx() }
        } else {
            Float.MAX_VALUE
        }

        val fontSize = remember(words.map{it.text}, maxWidthPx, maxHeightPx, constrainHeight) {
            computeMaxFontSize(
                words = words.map { it.text },
                maxWidthPx = maxWidthPx,
                maxHeightPx = maxHeightPx,
                minFontSize = minFontSize,
                maxFontSize = maxFontSize,
                fontFamily = fontFamily,
                textMeasurer = textMeasurer
            )
        }

        Row(
            modifier = if (constrainHeight) Modifier.fillMaxSize() else Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            words.forEach { word ->
                Text(
                    text = word.text,
                    fontSize = fontSize,
                    fontFamily = fontFamily,
                    maxLines = 1,
                    softWrap = false,
                    color = textColor(word.id)
                )
            }
        }
    }
}

private fun computeMaxFontSize(
    words: List<String>,
    maxWidthPx: Float,
    maxHeightPx: Float,
    minFontSize: TextUnit,
    maxFontSize: TextUnit,
    fontFamily: FontFamily,
    textMeasurer: TextMeasurer
): TextUnit {
    if (maxWidthPx <= 0f || maxHeightPx <= 0f || words.isEmpty()) return minFontSize

    var low = minFontSize.value
    var high = maxFontSize.value
    var best = minFontSize.value

    repeat(8) { // ~8 iterations is plenty of precision for sp steps
        val mid = (low + high) / 2f
        if (wordsFit(words, mid.sp, maxWidthPx, maxHeightPx, fontFamily, textMeasurer)) {
            best = mid
            low = mid
        } else {
            high = mid
        }
    }
    return best.sp
}

private fun wordsFit(
    words: List<String>,
    fontSize: TextUnit,
    maxWidthPx: Float,
    maxHeightPx: Float,
    fontFamily: FontFamily,
    textMeasurer: TextMeasurer
): Boolean {
    var totalWidth = 0f
    var maxLineHeight = 0f

    for (word in words) {
        val result = textMeasurer.measure(
            text = AnnotatedString(word),
            style = TextStyle(fontSize = fontSize, fontFamily = fontFamily)
        )
        totalWidth += result.size.width
        totalWidth += (words.size - 1) * 2 // min spacing between words
        maxLineHeight = maxOf(maxLineHeight, result.size.height.toFloat())
    }

    return maxLineHeight <= maxHeightPx && totalWidth <= maxWidthPx
}