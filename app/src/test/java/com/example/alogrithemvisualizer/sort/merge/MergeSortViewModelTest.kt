package com.example.alogrithemvisualizer.sort.merge

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(DelicateCoroutinesApi::class)
class MergeSortViewModelTest {
    private val viewModel = MergeSortViewModel()
    val thread = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(thread)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `construct splits last one should have single element each`() = runTest {
        val values = IntArray(20) { it + 1 }.apply { shuffle() }
        values.shuffle()
        viewModel.constructSplits()
        val last = viewModel.uiState.value.splits.last().flatMap { it.toList() }.toIntArray()
        assertArrayEquals(viewModel.uiState.value.values, last)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `merge sort last split should be one sorted list`() = runTest {
        val values = IntArray(20) { it + 1 }.apply { shuffle() }
        values.shuffle()
        viewModel.constructSplits()
        viewModel.mergeSplits()
        val last = viewModel.uiState.value.splits.last().flatMap { it.toList() }.toIntArray()
        assertArrayEquals(viewModel.uiState.value.values.sorted().toIntArray(), last)
    }


}