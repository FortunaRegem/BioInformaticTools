package hr.fer.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network

internal object NetworkAvailability {
	var isNetworkAvailable: Boolean = false

	fun checkInternet(context: Context) {
		val networkCallback: ConnectivityManager.NetworkCallback = object: ConnectivityManager.NetworkCallback() {
			override fun onAvailable(network: Network) {
				isNetworkAvailable = true
			}

			override fun onLost(network: Network) {
				isNetworkAvailable = false
			}
		}

		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		connectivityManager.registerDefaultNetworkCallback(networkCallback)
	}

}
