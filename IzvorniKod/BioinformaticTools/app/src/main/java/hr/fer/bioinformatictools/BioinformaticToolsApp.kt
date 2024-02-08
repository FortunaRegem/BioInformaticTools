package hr.fer.bioinformatictools

import android.app.Application
import hr.fer.bioinformatictools.di.BioinformaticToolsDI

class BioinformaticToolsApp : Application() {

	override fun onCreate() {
		super.onCreate()

		BioinformaticToolsDI.startInjection(applicationContext)
	}
}