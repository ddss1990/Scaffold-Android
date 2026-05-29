package com.base.androidscaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.base.androidscaffold.ui.theme.AndroidScaffoldTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            repository = (application as DemoApplication).appContainer.counterRepository,
        )
    }
    private val appLevelViewModel: AppLevelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidScaffoldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainRoute(
                        mainViewModel = mainViewModel,
                        appLevelViewModel = appLevelViewModel,
                    )
                }
            }
        }
    }
}

@Composable
private fun MainRoute(
    mainViewModel: MainViewModel,
    appLevelViewModel: AppLevelViewModel,
) {
    val state by mainViewModel.uiState.collectAsStateWithLifecycle()
    val appState by appLevelViewModel.uiState.collectAsStateWithLifecycle()
    MainScreen(
        state = state,
        appState = appState,
        onIncrement = mainViewModel::increment,
        onTrackAppEvent = appLevelViewModel::trackEvent,
    )
}

@Composable
private fun MainScreen(
    state: MainUiState,
    appState: AppLevelUiState,
    onIncrement: () -> Unit,
    onTrackAppEvent: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(
                R.string.main_status,
                state.title,
                state.counter,
                state.loaded,
            ),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(
                R.string.app_level_status,
                appState.applicationType,
                appState.eventCount,
            ),
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(onClick = onIncrement) {
            Text(text = stringResource(R.string.increment))
        }
        Button(onClick = onTrackAppEvent) {
            Text(text = stringResource(R.string.track_app_event))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    AndroidScaffoldTheme {
        MainScreen(
            state = MainUiState(counter = 1, loaded = true),
            appState = AppLevelUiState(applicationType = "DemoApplication", eventCount = 2),
            onIncrement = {},
            onTrackAppEvent = {},
        )
    }
}
