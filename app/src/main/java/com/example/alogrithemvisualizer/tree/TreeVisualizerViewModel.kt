package com.example.alogrithemvisualizer.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TreeVisualizerViewModel : ViewModel() {
    private val waitDuration = (300).milliseconds
    private val _uiState = MutableStateFlow(TreeVisualizerScreenState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: TreeVisualizerScreenEvent) = viewModelScope.launch {
        when (event) {
            is TreeVisualizerScreenEvent.AddNode -> addNode()
            is TreeVisualizerScreenEvent.Delete -> delete()
            is TreeVisualizerScreenEvent.Search -> search()
            is TreeVisualizerScreenEvent.UpdateValue -> _uiState.value =
                _uiState.value.copy(value = event.value)
        }
        delay((1).seconds)
        _uiState.value = _uiState.value.reset()
    }

    private suspend fun addNode() {
        val value = _uiState.value.value.toIntOrNull() ?: return
        if (_uiState.value.root == null) {
            _uiState.value = _uiState.value.copy(root = TreeNode(value))
            return
        }
        var current = _uiState.value.root
        while (current != null) {
            _uiState.value = _uiState.value.copy(searchNode = current)
            delay(waitDuration)
            when {
                current.value > value -> {
                    if (current.left != null)
                        current = current.left
                    else {
                        current.left = TreeNode(value)
                        break
                    }
                }

                current.value == value -> {
                    break
                }

                else -> {
                    if (current.right != null)
                        current = current.right
                    else {
                        current.right = TreeNode(value)
                        break
                    }
                }
            }
        }
    }

    private suspend fun search(): TreeNode? {
        val value = _uiState.value.value.toIntOrNull() ?: return null
        var current = _uiState.value.root
        while (current != null) {
            _uiState.value = _uiState.value.copy(searchNode = current)
            delay(waitDuration)
            if (current.value == value) {
                break
            }
            current = if (current.value > value) {
                current.left
            } else {
                current.right
            }
        }
        return current
    }

    private suspend fun delete() {
        val value = _uiState.value.value.toIntOrNull() ?: return
        var current = _uiState.value.root
        var parent: TreeNode? = null
        var linkDirection = 'l'
        while (current != null) {
            _uiState.value = _uiState.value.copy(searchNode = current)
            delay(waitDuration)
            if (current.value == value) {
                break
            }
            parent = current
            current = if (current.value > value) {
                linkDirection = 'l'
                current.left
            } else {
                linkDirection = 'r'
                current.right
            }
        }
        if (current == null) return
        _uiState.value = _uiState.value.copy(deleteNode = current)
        when {
            current.left == null && current.right == null -> {
                if (linkDirection == 'l')
                    parent?.left = null
                else
                    parent?.right = null
            }

            current.left != null -> {
                val (successor, successorParent) = findNextSuccessor(current.left!!, current)
                if (successorParent == current)
                    current.left = successor?.left
                else
                    successorParent?.right = null
                current.value = successor!!.value
            }

            else -> {
                if (parent == null)
                    _uiState.value = _uiState.value.copy(root = current.right)
                else
                    parent.right = current.right
            }
        }
    }

    private suspend fun findNextSuccessor(
        node: TreeNode,
        parent: TreeNode?
    ): Pair<TreeNode?, TreeNode?> {
        if (node.right == null) return node to parent
        _uiState.value = _uiState.value.copy(searchNode = node)
        return findNextSuccessor(node.right!!, node).also { delay(waitDuration) }
    }
}