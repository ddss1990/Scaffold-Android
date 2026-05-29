package com.base.androidscaffold

import com.base.androidscaffold.data.CounterRepository
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
        val viewModel = MainViewModel(
            repository = CounterRepository(
                title = "Test title",
                step = 1,
            ),
            loadDispatcher = UnconfinedTestDispatcher(testScheduler),
        )

        assertEquals(false, viewModel.uiState.value.loaded)
        advanceUntilIdle()
        assertEquals(true, viewModel.uiState.value.loaded)
        assertEquals("Test title", viewModel.uiState.value.title)
    }

    @Test
    fun incrementsCounter() = runTest {
        val viewModel = MainViewModel(
            repository = CounterRepository(
                title = "Test title",
                step = 2,
            ),
            loadDispatcher = UnconfinedTestDispatcher(testScheduler),
        )

        viewModel.increment()
        assertEquals(2, viewModel.uiState.value.counter)
    }
}
