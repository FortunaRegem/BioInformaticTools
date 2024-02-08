package hr.fer.network

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

internal object NetworkInterceptors {

	val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
		.apply {
			level = HttpLoggingInterceptor.Level.BODY
		}

	val headerInterceptor = Interceptor { chain ->
		val newRequest = chain.request()
			.newBuilder()
			.addHeader("Content-Type", "application/json")
			.build()

		Timber.tag("URL_TAG").d(newRequest.url.toString())

		if (NetworkAvailability.isNetworkAvailable) {
			NetworkErrors.checkResponse(chain.proceed(newRequest))
		} else {
			throw NetworkErrors.InternetException()
		}
	}

}