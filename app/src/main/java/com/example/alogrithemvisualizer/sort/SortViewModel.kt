package com.example.alogrithemvisualizer.sort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
class SortViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SortVisualizationState())
    val uiState = _uiState.asStateFlow()
    private val waitDuration = (300).milliseconds

    init {
        val values = IntArray(100) { Random.nextInt(100, 10000) }
        _uiState.value = SortVisualizationState(
            bubbleSort = SortVisualizationState.SortData(values = values.toMutableList()),
            insertionSort = SortVisualizationState.SortData(values = values.toMutableList()),
            selectionSort = SortVisualizationState.SortData(values = values.toMutableList()),
        )
        viewModelScope.launch {
            delay((1).seconds)
            launch {
                measureTime {
                    bubbleSort()
                }.also { duration ->
                    _uiState.update { it.copy(bubbleSort = it.bubbleSort.copy(time = duration)) }
                }
            }
            launch {
                measureTime {
                    insertionSort()
                }.also { duration ->
                    _uiState.update {
                        it.copy(
                            insertionSort = it.insertionSort.copy(time = duration)
                        )
                    }
                }
            }

            launch {
                measureTime {
                    selectionSort()
                }.also { duration ->
                    _uiState.update {
                        it.copy(
                            selectionSort = it.selectionSort.copy(time = duration)
                        )
                    }
                }
            }
        }

    }

    private suspend fun bubbleSort() {
        val values = _uiState.value.bubbleSort.values
        for (i in values.indices) {
            delay(waitDuration)
            for (j in 0 until values.size - i - 1) {
                if (values[j] > values[j + 1]) {
                    val temp = values[j]
                    values[j] = values[j + 1]
                    values[j + 1] = temp
                    _uiState.update {
                        it.copy(
                            bubbleSort = it.bubbleSort.copy(
                                values = values.toMutableList(),
                                activeIndex = j
                            )
                        )
                    }
                }
            }
        }
        _uiState.update { it.copy(bubbleSort = it.bubbleSort.copy(activeIndex = null)) }
    }

    private suspend fun insertionSort() {
        val values = _uiState.value.insertionSort.values
        for (i in 1 until values.size) {
            _uiState.update { it.copy(insertionSort = it.insertionSort.copy(activeIndex = i)) }
            delay(waitDuration)
            val key = values[i]
            var j = i - 1
            while (j >= 0 && values[j] > key) {
                values[j + 1] = values[j]
                j--
            }
            values[j + 1] = key
            _uiState.update {
                it.copy(
                    insertionSort = it.insertionSort.copy(
                        values = values.toMutableList(),
                        activeIndex = j + 1
                    )
                )
            }
        }
        _uiState.update { it.copy(insertionSort = it.insertionSort.copy(activeIndex = null)) }
    }

    private suspend fun selectionSort() {
        val values = _uiState.value.selectionSort.values
        for (i in values.indices) {
            _uiState.update { it.copy(selectionSort = it.selectionSort.copy(activeIndex = i)) }
            delay(waitDuration)
            var minIndex = i
            for (j in i + 1 until values.size) {
                if (values[j] < values[minIndex]) {
                    minIndex = j
                }
            }
            val temp = values[minIndex]
            values[minIndex] = values[i]
            values[i] = temp
            _uiState.update {
                it.copy(
                    selectionSort = it.selectionSort.copy(
                        values = values.toMutableList(),
                        activeIndex = minIndex
                    )
                )
            }
        }
        _uiState.update {
            it.copy(selectionSort = it.selectionSort.copy(activeIndex = null))
        }
    }
}