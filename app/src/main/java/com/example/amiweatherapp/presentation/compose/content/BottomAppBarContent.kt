package com.example.amiweatherapp.presentation.compose.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.amiweatherapp.R
import com.example.amiweatherapp.presentation.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun BottomAppBarContent(homeViewModel: HomeViewModel, onBottomSheetOpen: () -> Unit) {
    BottomAppBar(
        modifier = Modifier.height(64.dp),
        containerColor = Color.Transparent,
        content = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                IconButton(onClick = {
                    homeViewModel.viewModelScope.launch {
                        homeViewModel.fetchForecast()
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_navigation),
                        contentDescription = "Navigation icon"
                    )
                }
                IconButton(onClick = { onBottomSheetOpen() }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                }
            }
        }
    )
}