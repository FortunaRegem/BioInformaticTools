package hr.fer.network

import okhttp3.Response
import java.io.IOException

internal object NetworkErrors {

	const val NO_DATA = "No data currently available."
	const val NO_INTERNET = "Please check your internet access!"

	class InternetException: IOException() {
		override val message: String
			get() = NO_INTERNET
	}

	class NoDataException: IOException() {
		override val message: String
			get() = NO_DATA
	}

	fun checkResponse(response: Response): Response {
		return when(response.code) {
			404 -> throw NoDataException()
			else -> response
		}
	}
}