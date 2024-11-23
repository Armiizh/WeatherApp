package com.example.amiweatherapp.presentation.compose.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarContent(isTextFieldVisible: Boolean, changeVisible: () -> Unit) {

    TopAppBar(
        title = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "AmiWeatherApp")
                IconButton(
                    onClick = { changeVisible() }
                ) {
                    if (isTextFieldVisible) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "CloseIcon"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "SearchIcon"
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}