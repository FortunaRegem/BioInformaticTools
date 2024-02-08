package hr.fer.common.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment

fun Activity.call(phoneNumber: String) {
	val intent = Intent(
		Intent.ACTION_VIEW,
		Uri.parse("tel:$phoneNumber")
	)

	this.startActivity(Intent.createChooser(intent, ""))
}

fun Activity.driveToLocation(latitude: Double, longitude: Double) {
	val intent = Intent(
		Intent.ACTION_VIEW,
		Uri.parse("google.navigation:q=$latitude,$longitude")
	)

	this.startActivity(Intent.createChooser(intent, ""))
}

fun Activity.showAppSettings() {
	val settings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
	settings.data = Uri.parse("package:" + this.packageName)

	this.startActivity(Intent.createChooser(settings, ""))
}

fun Fragment.call(phoneNumber: String) = this.activity?.call(phoneNumber)

fun Fragment.driveToLocation(latitude: Double, longitude: Double) = this.activity?.driveToLocation(latitude, longitude)