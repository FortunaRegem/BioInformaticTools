package hr.fer.localsequencealigner.navigation

import android.content.Context
import android.content.Intent
import hr.fer.localsequencealigner.R
import hr.fer.localsequencealigner.ui.LocalSequenceAlignerActivity
import hr.fer.navigation.NavigationDirections

class LocalSequenceAlignerNavigation(private val context: Context) : NavigationDirections {

	override fun getFeatureName(): String = "Smith-Waterman local alignment"
	override fun getFeatureIcon(): Int = R.drawable.ic_dna
	override fun getNavigationIntent(): Intent = Intent(context, LocalSequenceAlignerActivity::class.java)

	override fun navigateToFeature() {
		val intent = getNavigationIntent()
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		context.startActivity(intent)
	}
}