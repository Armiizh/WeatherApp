package com.example.amiweatherapp.presentation.compose

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.presentation.HomeViewModel
import kotlin.math.roundToInt

@Composable
fun TopSection(
    viewModel: HomeViewModel,
    result: Result.Success<WeatherResponse>,
    ctx: Context
) {
    Text(
        text = "${result.data.location.region}, ${result.data.location.country}",
        fontSize = 12.sp,
        color = Color.White
    )
    Text(
        text = "${result.data.location.lat}, ${result.data.location.lon}",
        fontSize = 12.sp,
        color = Color.White
    )
    val isTempInFahrenheit by viewModel.isTempInFahrenheit.collectAsState()
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val iconUrl = result.data.current.condition.icon
        AsyncImage(
            modifier = Modifier.size(80.dp),
            model = ImageRequest.Builder(ctx)
                .data("https:$iconUrl")
                .size(Size.ORIGINAL)
                .build(),
            contentDescription = null
        )

        Text(
            text = if (isTempInFahrenheit) {
                "${result.data.current.temp_f.roundToInt()}°"
            } else {
                "${result.data.current.temp_c.roundToInt()}°"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 64.sp,
            color = Color.White
        )
    }
    Text(
        text = if (isTempInFahrenheit) {
            "Ощущается как ${result.data.current.feelslike_f.roundToInt()}°"
        } else {
            "Ощущается как ${result.data.current.feelslike_c.roundToInt()}°"
        },
        fontSize = 12.sp,
        color = Color.White
    )
}