package com.ghostdev.skycast.presentation.screens

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.ghostdev.skycast.R
import com.ghostdev.skycast.databinding.ActivityLoadingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class Loading : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding
    var lat: Double = 2.0
    var long: Double = 0.0
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.loading)
            .into(binding.loading)

        locationRequest = LocationRequest.create().apply {
            interval = 500 // Interval in milliseconds at which you want to receive location updates
            fastestInterval = 500 // Fastest interval in milliseconds for location updates
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Set the priority for location updates
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    lat = location.latitude
                    long = location.longitude
                }
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        gpsEnabled(isGpsEnabled)
        fetchLocation()
    }
    private fun gpsEnabled(gpsEnabled: Boolean) {
        if (!gpsEnabled) {
            AlertDialog.Builder(this)
                .setTitle("Location Services Disabled")
                .setMessage("Please enable location services to proceed.")
                .setPositiveButton("Enable") { _, _ ->
                    // Open location settings
                    val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(settingsIntent)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // User canceled, handle accordingly
                    Toast.makeText(this, "Please enable location", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }
        task.addOnSuccessListener {
            if (it != null) {
                binding.loadingText.text = "Fetching weather data succeeded."
                val handler = Handler()
                handler.postDelayed({
                    // Handle the case when the timeout is reached
                }, 1000)
                lat = getLat(it.latitude)
                long = getLong(it.longitude)

                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra("location", "$lat, $long")
                startActivity(intent)
                finish()

            } else {
                // Request location updates
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null)
            }
            when (it == null) {
                true -> fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null)
                else -> {
                }
            }
        }
    }


    private fun getLat(latitude: Double): Double {
        return latitude
    }
    private fun getLong(longitude: Double): Double {
        return longitude
    }

    override fun onResume() {
        super.onResume()
        fetchLocation()
    }
}