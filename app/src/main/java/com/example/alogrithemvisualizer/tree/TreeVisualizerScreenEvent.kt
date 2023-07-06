package com.example.alogrithemvisualizer.tree

sealed interface TreeVisualizerScreenEvent {
    object AddNode : TreeVisualizerScreenEvent
    object Delete : TreeVisualizerScreenEvent
    object Search : TreeVisualizerScreenEvent
    data class UpdateValue(val value: String) : TreeVisualizerScreenEvent
}