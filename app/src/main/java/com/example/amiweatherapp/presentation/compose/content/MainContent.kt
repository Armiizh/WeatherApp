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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amiweatherapp.R
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.presentation.HomeViewModel
import com.example.amiweatherapp.presentation.compose.CurrentWeatherInfo
import com.example.amiweatherapp.presentation.compose.ForecastList
import com.example.amiweatherapp.presentation.compose.HourlyForecast
import com.example.amiweatherapp.presentation.compose.LoadingDataContent
import com.example.amiweatherapp.presentation.compose.TopSection

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(
    viewModel: HomeViewModel,
    paddingValues: PaddingValues,
    isTextFieldVisible: Boolean,
    showBottomSheet: Boolean,
    changeVisible: () -> Unit,
    hideBottomSheet: () -> Unit
) {

    val dataIsLoading by viewModel.dataIsLoading.collectAsState()

    if (dataIsLoading) {
        LoadingDataContent()
    } else {

        if (showBottomSheet) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = {
                    hideBottomSheet()
                },
                sheetState = sheetState,
                containerColor = colorResource(id = R.color.lightBlue)
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
            if (isTextFieldVisible) {
                SearchContent(viewModel) { changeVisible() }
            } else {
                val ctx = LocalContext.current
                val weatherData by viewModel.weatherData.collectAsState()

                when (val result = weatherData) {
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
            }
        }
    }
}