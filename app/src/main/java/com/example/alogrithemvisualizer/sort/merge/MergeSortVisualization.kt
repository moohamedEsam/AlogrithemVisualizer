package com.example.alogrithemvisualizer.sort.merge

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun MergeSortVisualization(
    viewModel: MergeSortViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    MergeSortVisualization(state)
}

@Composable
private fun MergeSortVisualization(
    state: MergeSortState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            modifier = Modifier.heightIn(min = 40.dp, max = 400.dp)
        ) {
            items(state.values.toList()) { value ->
                ValueItem(value)
            }
        }
        state.splits.forEach { splits ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                splits.forEach { split ->
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 40.dp),
                        modifier = Modifier
                            .width(((LocalConfiguration.current.screenWidthDp / splits.size) + 10).dp)
                            .heightIn(min = 40.dp, max = 400.dp)
                    ) {
                        items(split.toList()) { value ->
                            ValueItem(value = value)
                        }
                    }

                }
            }
        }

    }
}

@Composable
private fun ValueItem(value: Int) {
    Text(
        text = value.toString(),
        modifier = Modifier.border(
            1.dp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
private fun MergeSortVisualizationPreview() {
    Surface {
        MergeSortVisualization(
            MergeSortState(
                values = IntArray(100) { it },
                splits = mutableListOf(
                    listOf(
                        intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                        intArrayOf(10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
                    ),
                    listOf(
                        intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                        intArrayOf(10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
                    ),

                    listOf(
                        intArrayOf(1, 2, 3),
                        intArrayOf(4),
                        intArrayOf(5, 6, 7),
                    )
                )
            )
        )
    }

}