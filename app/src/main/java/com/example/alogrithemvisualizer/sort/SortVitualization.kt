package com.example.alogrithemvisualizer.sort

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import com.example.alogrithemvisualizer.DraggableCanvas
import org.koin.androidx.compose.koinViewModel
import kotlin.time.DurationUnit

@Composable
fun SortVisualization(viewModel: SortViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    SortVisualization(state)
}


@OptIn(ExperimentalTextApi::class)
@Composable
private fun SortVisualization(
    state: SortVisualizationState
) {
    val color = MaterialTheme.colorScheme.onBackground
    val barColor = Color.LightGray
    val activeBarColor = MaterialTheme.colorScheme.primary
    val textMeasurer = rememberTextMeasurer()
    DraggableCanvas {
        var endY = sortGraph(
            color = color,
            textMeasurer = textMeasurer,
            activeBarColor = activeBarColor,
            barColor = barColor,
            title = "Bubble Sort",
            data = state.bubbleSort
        )
        endY = sortGraph(
            color = color,
            textMeasurer = textMeasurer,
            activeBarColor = activeBarColor,
            barColor = barColor,
            title = "Insertion Sort",
            startY = endY + 20f,
            data = state.insertionSort
        )
        sortGraph(
            color = color,
            textMeasurer = textMeasurer,
            activeBarColor = activeBarColor,
            barColor = barColor,
            title = "Selection Sort",
            startY = endY + 20f,
            data = state.selectionSort
        )
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.sortGraph(
    color: Color,
    textMeasurer: TextMeasurer,
    activeBarColor: Color,
    barColor: Color,
    title: String,
    startY: Float = 0f,
    data: SortVisualizationState.SortData
): Float {
    drawPoints(
        points = listOf(
            Offset(150f, 80 + startY),
            Offset(150f, size.height / 4 + startY),
        ),
        pointMode = PointMode.Polygon,
        color = color,
        strokeWidth = 15f
    )
    drawScales(textMeasurer, startY, color)

    data.values.forEachIndexed { index, value ->
        val x = 150f + (index + 1) * 10f
        val y = (size.height / 4) + startY - value / 20f
        drawLine(
            color = if (index == data.activeIndex) activeBarColor else barColor,
            start = Offset(x, size.height / 4 + startY),
            end = Offset(x, y),
            strokeWidth = 5f
        )
        if (index == data.values.lastIndex)
            drawLine(
                color = color,
                start = Offset(150f, size.height / 4 + startY),
                end = Offset(x + 10f, size.height / 4 + startY),
                strokeWidth = 10f
            )
    }
    val titleResult = textMeasurer.measure(buildAnnotatedString {
        append(title)
        if (data.time != null)
            append(" - ${data.time.toLong(DurationUnit.MILLISECONDS)} ms")
    })
    drawText(
        titleResult,
        topLeft = Offset(
            (size.width - titleResult.size.width) / 2,
            50 + startY
        ),
        color = color
    )
    return size.height / 4 + startY
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawScales(
    textMeasurer: TextMeasurer,
    startY: Float,
    color: Color
) {
    repeat(11) {
        val textResult = textMeasurer.measure(AnnotatedString((it * 1000).toString()))
        drawText(
            textResult,
            topLeft = Offset(
                150f - textResult.size.width - 40f,
                (size.height / 4) + startY - textResult.size.height - it * 45f
            ),
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SortVisualizationPreview() {
    Surface {
        SortVisualization(
            state = SortVisualizationState()
        )
    }

}