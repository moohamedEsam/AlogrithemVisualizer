package com.example.alogrithemvisualizer.sort.merge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class MergeSortViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        MergeSortState(
            values = IntArray(20) { it + 1 }.apply { shuffle() }
        )
    )
    val uiState = _uiState.asStateFlow()
    private val waitDuration = (1).seconds

    init {
        viewModelScope.launch {
            delay((1).seconds)
            constructSplits()
            mergeSplits()
        }
    }


     suspend fun constructSplits() {
        while (true) {
            val lastSplit = _uiState.value.splits.lastOrNull() ?: listOf(_uiState.value.values)
            val isComplete = lastSplit.all { it.size == 1 }
            if (isComplete) return
            val newSplit = mutableListOf<IntArray>()
            lastSplit.forEach {
                if (it.size == 1) {
                    newSplit.add(it)
                    return@forEach
                }
                val middle = it.size / 2
                val left = it.sliceArray(0 until middle)
                val right = it.sliceArray(middle until it.size)
                newSplit.add(left)
                newSplit.add(right)
            }
            _uiState.update {
                it.copy(
                    splits = it.splits + listOf(newSplit)
                )
            }
            delay(waitDuration)
        }
    }

    suspend fun mergeSplits() {
        while (true) {
            val lastSplit = _uiState.value.splits.lastOrNull() ?: return
            val isComplete =
                 lastSplit.size == 1 && lastSplit.first().size == _uiState.value.values.size
            if (isComplete) return
            val newSplit = mutableListOf<IntArray>()
            var counter = 0
            while (counter < lastSplit.size - 1) {
                val left = lastSplit[counter]
                val right = lastSplit[counter + 1]
                newSplit.add(merge(left, right))
                counter += 2
            }
            if(lastSplit.size % 2 == 1) {
                newSplit.add(lastSplit.last())
            }
            _uiState.update {
                it.copy(
                    splits = it.splits + listOf(newSplit)
                )
            }
            delay(waitDuration)
        }
    }


    private fun merge(left: IntArray, right: IntArray): IntArray {
        var indexLeft = 0
        var indexRight = 0
        val newList = mutableListOf<Int>()
        while (indexLeft < left.size && indexRight < right.size) {
            if (left[indexLeft] <= right[indexRight]) {
                newList.add(left[indexLeft])
                indexLeft++
            } else {
                newList.add(right[indexRight])
                indexRight++
            }
        }
        while (indexLeft < left.size) {
            newList.add(left[indexLeft])
            indexLeft++
        }
        while (indexRight < right.size) {
            newList.add(right[indexRight])
            indexRight++
        }
        return newList.toIntArray()
    }

}