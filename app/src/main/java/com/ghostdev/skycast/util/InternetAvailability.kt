package com.ghostdev.skycast.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class InternetAvailability {
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null) {
            // For devices with Android 10 and above, use NetworkCapabilities to check internet connectivity
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            ))
        }

        return false
    }
}