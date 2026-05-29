package com.base.androidscaffold

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppLevelUiState(
    val applicationType: String,
    val eventCount: Int = 0,
)

class AppLevelViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(
        AppLevelUiState(
            applicationType = application.javaClass.simpleName,
        ),
    )
    val uiState: StateFlow<AppLevelUiState> = _uiState.asStateFlow()

    fun trackEvent() {
        _uiState.update { it.copy(eventCount = it.eventCount + 1) }
    }
}
