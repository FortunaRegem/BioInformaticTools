package hr.fer.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
	fun createNetworkClient(baseUrl: String) = retrofitClient(baseUrl, httpClient())

	fun checkInternetConnection(context: Context) = NetworkAvailability.checkInternet(context)

	private fun retrofitClient(baseUrl: String, httpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl(baseUrl)
			.client(httpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	private fun httpClient(): OkHttpClient {
		val clientBuilder = OkHttpClient.Builder()
			.addInterceptor(NetworkInterceptors.httpLoggingInterceptor)
			.addInterceptor(NetworkInterceptors.headerInterceptor)

		return clientBuilder.build()
	}
}

