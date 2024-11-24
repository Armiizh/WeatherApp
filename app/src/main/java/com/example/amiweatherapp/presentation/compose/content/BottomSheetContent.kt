package com.example.amiweatherapp.presentation.compose.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amiweatherapp.presentation.HomeViewModel

@Composable
fun BottomSheetContent(viewModel: HomeViewModel, onClose: () -> Unit) {

    val isSpeedInMph by viewModel.isSpeedInMph.collectAsState()
    val isTempInFahrenheit by viewModel.isTempInFahrenheit.collectAsState()
    val isVisibilityInMiles by viewModel.isVisibilityInMiles.collectAsState()
    val isPressureInInch by viewModel.isPressureInInch.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            text = "Настройки единиц измерения",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Переключатель для температуры
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Температура в Фаренгейтах",
                modifier = Modifier.weight(1f),
                color = Color.White
            )
            Switch(
                checked = isTempInFahrenheit,
                onCheckedChange = { viewModel.setTempInFahrenheit(it) }
            )
        }
        //Переключатель для скорости
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Скорость в милях/ч", modifier = Modifier.weight(1f), color = Color.White)
            Switch(
                checked = isSpeedInMph,
                onCheckedChange = { viewModel.setSpeedInMph(it) }
            )
        }

        //Переключатель для давления
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Давление в дюймах", modifier = Modifier.weight(1f), color = Color.White)
            Switch(
                checked = isPressureInInch,
                onCheckedChange = { viewModel.setPressureInInch(it) }
            )
        }

        //Переключатель для дальности видимости
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Дальность видимости в милях",
                modifier = Modifier.weight(1f),
                color = Color.White
            )
            Switch(
                checked = isVisibilityInMiles,
                onCheckedChange = { viewModel.setVisibilityInMiles(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Кнопка закрытия BottomSheet
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { onClose() }) {
                Text("Закрыть")
            }
        }
    }
}