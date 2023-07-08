package com.example.alogrithemvisualizer.sort

import kotlin.random.Random
import kotlin.time.Duration

data class SortVisualizationState(
    val bubbleSort: SortData = SortData(),
    val insertionSort: SortData = SortData(),
    val selectionSort: SortData = SortData(),
) {
    data class SortData(
        val values: MutableList<Int> = MutableList(100) { Random.nextInt(100, 10000) },
        val activeIndex: Int? = null,
        val time: Duration? = null
    )
}