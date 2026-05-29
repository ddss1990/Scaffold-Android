package com.base.androidscaffold.data

import kotlinx.coroutines.delay

class CounterRepository(
    private val title: String,
    private val step: Int,
) {
    suspend fun loadTitle(): String {
        delay(200)
        return title
    }

    fun nextCounter(current: Int): Int = current + step
}
