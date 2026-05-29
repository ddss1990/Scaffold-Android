package com.base.androidscaffold

import android.app.Application
import com.base.androidscaffold.data.CounterRepository

class DemoApplication : Application() {
    val appContainer: AppContainer by lazy {
        AppContainer(
            counterRepository = CounterRepository(
                title = "Factory + Repository parameter",
                step = 2,
            ),
        )
    }
}

data class AppContainer(
    val counterRepository: CounterRepository,
)
