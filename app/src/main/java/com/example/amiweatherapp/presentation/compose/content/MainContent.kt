package com.example.amiweatherapp.presentation.compose.content

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.data.utils.getErrorMessage
import com.example.amiweatherapp.presentation.HomeViewModel
import com.example.amiweatherapp.presentation.compose.CurrentWeatherInfo
import com.example.amiweatherapp.presentation.compose.ForecastList
import com.example.amiweatherapp.presentation.compose.HourlyForecast
import com.example.amiweatherapp.presentation.compose.TopSection
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(
    viewModel: HomeViewModel,
    paddingValues: PaddingValues,
    showBottomSheet: Boolean,
    hideBottomSheet: () -> Unit
) {

    var isDay by remember { mutableIntStateOf(0) }

    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = {
                hideBottomSheet()
            },
            sheetState = sheetState,
            containerColor = if (isDay == 1) {
                Color.LightGray.copy(alpha = .95f)
            } else {
                Color.DarkGray.copy(alpha = .95f)
            }
        ) {
            BottomSheetContent(viewModel) { hideBottomSheet() }
        }
    }
    Column(
        Modifier
            .padding(paddingValues)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        val ctx = LocalContext.current
        val weatherData by viewModel.weatherData.collectAsState()

        when (val result = weatherData) {
            is Result.Success -> {

                val currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"))
                val todayForecast = result.data.forecast.forecastday.firstOrNull {
                    it.date == LocalDateTime.now().toLocalDate().toString()
                }
                val currentHourData = todayForecast?.hour?.find { hour ->
                    hour.time.split(" ")[1].substring(0, 2) == currentHour
                }
                val current = result.data.current
                isDay = currentHourData?.is_day ?: current.is_day

                Text(
                    text = result.data.location.name,
                    fontSize = 36.sp,
                    color = Color.White
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopSection(viewModel, result, ctx)
                    Spacer(Modifier.height(48.dp))
                    HourlyForecast(viewModel, result, ctx)
                    Spacer(Modifier.height(8.dp))
                    ForecastList(viewModel, result, ctx)
                    Spacer(Modifier.height(8.dp))
                    CurrentWeatherInfo(viewModel, result)
                }
            }

            is Result.Error -> {
                val errorMessage = getErrorMessage(result.error)
                Column(
                    Modifier.fillMaxSize(),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Text(text = errorMessage)
                    Button(onClick = {
                        viewModel.viewModelScope.launch {
                            viewModel.fetchForecast()
                        }
                    }) {
                        Text(text = "Попробовать еще раз")
                    }
                }
                Toast.makeText(ctx, errorMessage, Toast.LENGTH_SHORT).show()
            }

            null -> {
                Text(text = "Что-то пошло не так :(")
            }
        }
    }
}