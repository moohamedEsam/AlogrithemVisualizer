package com.example.alogrithemvisualizer.tree

data class TreeVisualizerScreenState(
    val root: TreeNode? = null,
    val searchNode: TreeNode? = null,
    val deleteNode: TreeNode? = null,
    val value: String = ""
){
    fun reset() = copy(
        searchNode = null,
        deleteNode = null,
    )
}