package com.example.alogrithemvisualizer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DraggableCanvas(content: DrawScope.() -> Unit) {
    var offset by remember {
        mutableStateOf(IntOffset(0, 0))
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = rememberDraggableState(onDelta = {
                    offset += IntOffset(it.roundToInt(), 0)
                }),
                orientation = Orientation.Horizontal
            )
            .draggable(
                state = rememberDraggableState(onDelta = {
                    offset += IntOffset(0, it.roundToInt())
                }),
                orientation = Orientation.Vertical
            )
            .offset(offset.x.dp, offset.y.dp)
    ) {
        content()
    }
}