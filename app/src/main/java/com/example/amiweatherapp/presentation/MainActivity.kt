package com.example.amiweatherapp.presentation

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amiweatherapp.presentation.compose.ui.theme.AmiWeatherAppTheme
import com.example.amiweatherapp.presentation.screen.HomeScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            AmiWeatherAppTheme {
                val locationPermissionState =
                    rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
                val locationViewModel: LocationViewModel = viewModel()

                LaunchedEffect(Unit) {
                    if (locationPermissionState.hasPermission) {
                        requestCurrentLocation(locationViewModel)
                    } else {
                        locationPermissionState.launchPermissionRequest()
                    }
                }

                LaunchedEffect(locationPermissionState.permissionRequested) {
                    if (locationPermissionState.hasPermission) {
                        requestCurrentLocation(locationViewModel)
                    }
                }
                if (locationPermissionState.hasPermission) {
                    HomeScreen()
                }
            }
        }
    }

    private fun requestCurrentLocation(locationViewModel: LocationViewModel) {

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMaxUpdates(1)
            .build()

        fusedLocationClient.requestLocationUpdates(locationRequest, object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                locationResult.lastLocation?.let { location ->
                    locationViewModel.updateLocation(location, application)
                    fusedLocationClient.removeLocationUpdates(this)
                } ?: run {
                    Toast.makeText(application, "Что-то пошло не так, попробуй те позже", Toast.LENGTH_SHORT).show()
                    Log.d("CHECK", "Что то пошло не так при поиске геолокации место: MainActivity.requestCurrentLocation.onLocationResult")
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                if (!locationAvailability.isLocationAvailable) {
                    Toast.makeText(application, "Предоставьте пожалуйста разрешение на определение местоположениия", Toast.LENGTH_SHORT).show()
                    Log.d("CHECK", "Что то пошло не так при поиске геолокации место: MainActivity.requestCurrentLocation.onLocationAvailability")
                }
            }
        }, Looper.getMainLooper())
    }
}