package com.example.amiweatherapp.presentation.compose.content

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.amiweatherapp.presentation.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchContent(homeViewModel: HomeViewModel, changeVisible: () -> Unit) {
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