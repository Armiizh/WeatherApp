package com.example.amiweatherapp.presentation.screen

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.presentation.HomeViewModel
import com.example.amiweatherapp.presentation.compose.CurrentWeatherInfo
import com.example.amiweatherapp.presentation.compose.ForecastList
import com.example.amiweatherapp.presentation.compose.GradientBackground
import com.example.amiweatherapp.presentation.compose.HourlyForecast
import com.example.amiweatherapp.presentation.compose.LoadingDataContent
import com.example.amiweatherapp.presentation.compose.TopSection
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
    GradientBackground()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBarContent(isTextFieldVisible) { isTextFieldVisible = !isTextFieldVisible }
        },
        content = { paddingValues ->
            MainContent(homeViewModel, paddingValues, isTextFieldVisible) { isTextFieldVisible = !isTextFieldVisible }
        },
        bottomBar = {
            BottomAppBarContent(homeViewModel)
        }
    )
}



@Composable
fun SearchCity(homeViewModel: HomeViewModel, changeVisible: () -> Unit) {
    var city by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier
            .wrapContentWidth(),
        value = city,
        onValueChange = {
            city = it
        },
        placeholder = { Text(text = "Москва...", fontSize = 24.sp) },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        trailingIcon = {
            if (city.length > 1) {
                IconButton(
                    onClick = {
                        homeViewModel.viewModelScope.launch {
                            homeViewModel.fetchForecast(city)
                        }
                        changeVisible()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "SearchIcon"
                    )
                }
            }
        }
    )
}