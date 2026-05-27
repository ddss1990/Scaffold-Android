package com.base.androidscaffold

data class MainUiState(
    val title: String = "Lifecycle + ViewModel + Flow",
    val counter: Int = 0,
    val loaded: Boolean = false,
)
