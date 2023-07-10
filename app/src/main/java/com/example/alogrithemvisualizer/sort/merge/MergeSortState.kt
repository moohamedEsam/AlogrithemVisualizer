package com.example.alogrithemvisualizer.sort.merge

data class MergeSortState(
    val values: IntArray = intArrayOf(),
    val splits: List<List<IntArray>> = mutableListOf(),
)