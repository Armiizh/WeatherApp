package com.example.amiweatherapp.presentation.compose.content

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.presentation.HomeViewModel
import com.example.amiweatherapp.presentation.compose.CurrentWeatherInfo
import com.example.amiweatherapp.presentation.compose.ForecastList
import com.example.amiweatherapp.presentation.compose.HourlyForecast
import com.example.amiweatherapp.presentation.compose.LoadingDataContent
import com.example.amiweatherapp.presentation.compose.TopSection
import com.example.amiweatherapp.presentation.screen.SearchCity

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(
    homeViewModel: HomeViewModel,
    paddingValues: PaddingValues,
    isTextFieldVisible: Boolean,
    changeVisible: () -> Unit
) {
    val forecastFor7DaysData by homeViewModel.weatherData.collectAsState()
    val forecastFor7DaysIsLoading by homeViewModel.dataIsLoading.collectAsState()

    if (forecastFor7DaysIsLoading) {
        LoadingDataContent()
    } else {

        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            if (!isTextFieldVisible) {
                val ctx = LocalContext.current

                when (val result = forecastFor7DaysData) {
                    is Result.Success -> {
                        Text(
                            text = result.data.location.name,
                            fontSize = 36.sp
                        )
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TopSection(result, ctx)
                            Spacer(Modifier.height(48.dp))
                            HourlyForecast(result, ctx)
                            Spacer(Modifier.height(8.dp))
                            ForecastList(result, ctx)
                            Spacer(Modifier.height(8.dp))
                            CurrentWeatherInfo(result)
                        }
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            ctx,
                            "Ошибка: ${result.error}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        Log.d("CHECK", "FetchWeatherUseCase ошибка - ${result.error}")
                    }

                    null -> {
                        Text(text = "Введите город и нажмите 'Найти'")
                    }
                }
            } else {
                SearchCity(homeViewModel) { changeVisible() }
            }
        }
    }
}