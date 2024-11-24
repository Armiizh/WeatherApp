package com.example.amiweatherapp.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amiweatherapp.presentation.HomeViewModel
import com.example.amiweatherapp.presentation.compose.Background
import com.example.amiweatherapp.presentation.compose.LoadingDataContent
import com.example.amiweatherapp.presentation.compose.content.BottomAppBarContent
import com.example.amiweatherapp.presentation.compose.content.MainContent
import com.example.amiweatherapp.presentation.compose.content.TopAppBarContent
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
) {
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val dataIsLoading by viewModel.dataIsLoading.collectAsState()

    Background(viewModel)
    if (dataIsLoading) {
        Scaffold(
            containerColor = Color.Transparent,
            content = { paddingValues ->
                LoadingDataContent(paddingValues)
            }
        )
    } else {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBarContent(viewModel)
            },
            content = { paddingValues ->
                MainContent(
                    viewModel,
                    paddingValues,
                    showBottomSheet
                ) { showBottomSheet = false }
            },
            bottomBar = {
                BottomAppBarContent(viewModel) {
                    scope.launch { showBottomSheet = true }
                }
            }
        )
    }
}