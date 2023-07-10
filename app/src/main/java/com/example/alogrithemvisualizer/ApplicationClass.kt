package com.example.alogrithemvisualizer

import android.app.Application
import com.example.alogrithemvisualizer.sort.SortViewModel
import com.example.alogrithemvisualizer.sort.merge.MergeSortViewModel
import com.example.alogrithemvisualizer.tree.TreeVisualizerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationClass)
            modules(
                module {
                    viewModel { TreeVisualizerViewModel() }
                    viewModel { SortViewModel() }
                    viewModel { MergeSortViewModel() }
                }
            )
        }
    }
}