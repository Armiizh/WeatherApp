package com.example.amiweatherapp.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amiweatherapp.presentation.HomeViewModel
import com.example.amiweatherapp.presentation.compose.GradientBackground
import com.example.amiweatherapp.presentation.compose.content.BottomAppBarContent
import com.example.amiweatherapp.presentation.compose.content.MainContent
import com.example.amiweatherapp.presentation.compose.content.TopAppBarContent
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
) {

    var isTextFieldVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    GradientBackground()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBarContent(isTextFieldVisible) { isTextFieldVisible = !isTextFieldVisible }
        },
        content = { paddingValues ->
            MainContent(
                homeViewModel,
                paddingValues,
                isTextFieldVisible,
                showBottomSheet,
                changeVisible = {
                    isTextFieldVisible = !isTextFieldVisible
                },
                hideBottomSheet = { showBottomSheet = false }
            )
        },
        bottomBar = {
            BottomAppBarContent(homeViewModel) {
                scope.launch { showBottomSheet = true }
            }
        }
    )
}