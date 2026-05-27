package com.base.androidscaffold

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @Test
    fun emitsLoadedStateAfterCoroutineRuns() = runTest {
        val viewModel = MainViewModel(UnconfinedTestDispatcher(testScheduler))

        assertEquals(false, viewModel.uiState.value.loaded)
        advanceUntilIdle()
        assertEquals(true, viewModel.uiState.value.loaded)
    }

    @Test
    fun incrementsCounter() = runTest {
        val viewModel = MainViewModel(UnconfinedTestDispatcher(testScheduler))

        viewModel.increment()
        assertEquals(1, viewModel.uiState.value.counter)
    }
}
