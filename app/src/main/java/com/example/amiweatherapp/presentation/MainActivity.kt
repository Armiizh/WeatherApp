package com.example.amiweatherapp.presentation

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
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

                HomeScreen(locationPermissionState)
            }
        }
    }

    private fun requestCurrentLocation(locationViewModel: LocationViewModel) {
        Log.d("LocationUpdate", "Requesting current location...")
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : com.google.android.gms.location.LocationCallback() {

                override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        Log.d(
                            "LocationUpdate",
                            "New location: ${location.latitude}, ${location.longitude}"
                        )
                        locationViewModel.updateLocation(location)
                        fusedLocationClient.removeLocationUpdates(this)
                    } ?: run {
                        Log.d("LocationUpdate", "Location result is null")
                        getLastLocation(locationViewModel)
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    Log.d(
                        "LocationUpdate",
                        "Location availability: ${locationAvailability.isLocationAvailable}"
                    )
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun getLastLocation(locationViewModel: LocationViewModel) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.d(
                    "LocationUpdate",
                    "Last known location: ${location.latitude}, ${location.longitude}"
                )
                locationViewModel.updateLocation(location)
            } else {
                Log.d("LocationUpdate", "Last known location is null")
            }
        }.addOnFailureListener { exception ->
            Log.e("LocationUpdate", "Error getting last location: ${exception.message}")
        }
    }
}