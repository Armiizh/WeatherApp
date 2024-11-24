package com.example.amiweatherapp.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingDataContent(paddingValues: PaddingValues) {
    Column(
        Modifier.fillMaxSize()
            .padding(paddingValues),
        Arrangement.Center,
        Alignment.CenterHorizontally,
    ) {
        Text(text = "Определение вашего местоположения...")
        CircularProgressIndicator()
    }
}