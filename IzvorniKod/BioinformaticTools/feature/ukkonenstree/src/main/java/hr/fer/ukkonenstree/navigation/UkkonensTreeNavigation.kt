package hr.fer.ukkonenstree.navigation

import android.content.Context
import android.content.Intent
import hr.fer.ukkonenstree.R
import hr.fer.ukkonenstree.ui.UkkonensTreeActivity
import hr.fer.navigation.NavigationDirections

class UkkonensTreeNavigation(private val context: Context) : NavigationDirections {

	override fun getFeatureName(): String = "Suffix Tree - Ukkonens algorithm"
	override fun getFeatureIcon(): Int = R.drawable.ic_dna
	override fun getNavigationIntent(): Intent = Intent(context, UkkonensTreeActivity::class.java)

	override fun navigateToFeature() {
		val intent = getNavigationIntent()
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		context.startActivity(intent)
	}
}