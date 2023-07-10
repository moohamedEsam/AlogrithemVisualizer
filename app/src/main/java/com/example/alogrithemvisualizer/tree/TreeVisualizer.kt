package com.example.alogrithemvisualizer.tree

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.center
import com.example.alogrithemvisualizer.DraggableCanvas
import org.koin.androidx.compose.koinViewModel


@Composable
fun TreeVisualizerScreen(viewModel: TreeVisualizerViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        TreeVisualizerScreen(state)
        ActionRow(
            state = state,
            onEvent = viewModel::handleEvent,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ActionRow(
    state: TreeVisualizerScreenState,
    onEvent: (TreeVisualizerScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        OutlinedTextField(
            value = state.value,
            onValueChange = { onEvent(TreeVisualizerScreenEvent.UpdateValue(it)) },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        IconButton(onClick = { onEvent(TreeVisualizerScreenEvent.AddNode) }) {
            Icon(Icons.Default.Add, contentDescription = "Add node")
        }
        IconButton(onClick = { onEvent(TreeVisualizerScreenEvent.Search) }) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
        IconButton(onClick = { onEvent(TreeVisualizerScreenEvent.Delete) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun TreeVisualizerScreen(
    state: TreeVisualizerScreenState
) {
    val textMeasurer = rememberTextMeasurer()
    val color = MaterialTheme.colorScheme.primary

    DraggableCanvas {
        if (state.root != null)
            drawNode(
                textMeasurer,
                state.root,
                center.copy(y = 200f),
                horizontalSpacing = 200f,
                color = color,
                activeNodeValue = state.searchNode?.value,
                deleteNodeValue = state.deleteNode?.value
            )
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawNode(
    textMeasurer: TextMeasurer,
    node: TreeNode,
    nodeCenter: Offset,
    horizontalSpacing: Float,
    color: Color,
    activeNodeValue: Int?,
    deleteNodeValue: Int? = null,
) {
    val radius = size.minDimension / 9
    val nodeColor = when {
        deleteNodeValue == node.value -> Color.Red
        activeNodeValue == node.value -> Color.Green
        else -> color
    }
    val nodeStyle = when {
        deleteNodeValue == node.value -> Stroke(width = 10f, join = StrokeJoin.Bevel)
        activeNodeValue == node.value -> Fill
        else -> Stroke(width = 10f)
    }
    drawCircle(
        color = nodeColor,
        radius = radius,
        center = nodeCenter,
        style = nodeStyle
    )
    drawText(node, textMeasurer, nodeCenter, color)
    node.left?.let {
        drawLeftArrow(
            nodeCenter,
            radius,
            -horizontalSpacing,
            color
        )
        drawNode(
            textMeasurer = textMeasurer,
            node = it,
            nodeCenter = nodeCenter + Offset(
                -horizontalSpacing,
                100 + radius * 2
            ),
            horizontalSpacing = horizontalSpacing,
            color = color,
            activeNodeValue = activeNodeValue,
            deleteNodeValue = deleteNodeValue
        )
    }
    node.right?.let {
        drawRightNode(
            nodeCenter,
            radius,
            horizontalSpacing,
            color
        )
        drawNode(
            textMeasurer = textMeasurer,
            node = it,
            nodeCenter = nodeCenter + Offset(
                horizontalSpacing,
                100 + radius * 2
            ),
            horizontalSpacing = horizontalSpacing,
            color = color,
            activeNodeValue = activeNodeValue,
            deleteNodeValue = deleteNodeValue
        )

    }
}

private fun DrawScope.drawRightNode(
    nodeCenter: Offset,
    radius: Float,
    horizontalSpacing: Float,
    color: Color
) {
    drawPoints(
        points = listOf(
            nodeCenter + Offset(0f, radius),
            nodeCenter + Offset(0f, radius + 50),
            nodeCenter + Offset(horizontalSpacing, radius + 50),
            nodeCenter + Offset(horizontalSpacing, radius + 100),
        ),
        pointMode = PointMode.Polygon,
        color = color,
        strokeWidth = 8f
    )
}

private fun DrawScope.drawLeftArrow(
    nodeCenter: Offset,
    radius: Float,
    horizontalSpacing: Float,
    color: Color
) {
    drawPoints(
        points = listOf(
            nodeCenter + Offset(0f, radius),
            nodeCenter + Offset(0f, radius + 50),
            nodeCenter + Offset(horizontalSpacing, radius + 50),
            nodeCenter + Offset(horizontalSpacing, radius + 100),
        ),
        pointMode = PointMode.Polygon,
        color = color,
        strokeWidth = 8f
    )
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawText(
    node: TreeNode,
    textMeasurer: TextMeasurer,
    nodeCenter: Offset,
    color: Color
) {
    val text = AnnotatedString("${node.value}")
    val textResult = textMeasurer.measure(text)
    val offset = nodeCenter.let {
        val x = it.x - textResult.size.center.x
        val y = it.y - textResult.size.center.y
        Offset(x, y)
    }
    withTransform({
        translate(offset.x, offset.y)
    }) {
        textResult.multiParagraph.paint(drawContext.canvas, color = color)
    }
}

@Preview(showBackground = true)
@Composable
private fun TreeVisualizerPreview() {
    Surface {
        Column {
            val state = TreeVisualizerScreenState(
                root = TreeNode(
                    value = 5,
                    right = TreeNode(
                        value = 10,
//                        right = TreeNode(15, TreeNode(12))
                    ),
                    left = TreeNode(
                        value = 1,
                        left = TreeNode(0, TreeNode(-1)),
                        right = TreeNode(3, right = TreeNode(2))
                    )
                )
            )
            TreeVisualizerScreen(state = state)
        }
    }

}