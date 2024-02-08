package hr.fer.fmindextable.navigation

import android.content.Context
import android.content.Intent
import hr.fer.fmindextable.R
import hr.fer.fmindextable.ui.FMIndexTableActivity
import hr.fer.navigation.NavigationDirections

class FMIndexTableNavigation(private val context: Context) : NavigationDirections {

	override fun getFeatureName(): String = "FM Index"
	override fun getFeatureIcon(): Int = R.drawable.ic_dna
	override fun getNavigationIntent(): Intent = Intent(context, FMIndexTableActivity::class.java)

	override fun navigateToFeature() {
		val intent = getNavigationIntent()
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		context.startActivity(intent)
	}
}