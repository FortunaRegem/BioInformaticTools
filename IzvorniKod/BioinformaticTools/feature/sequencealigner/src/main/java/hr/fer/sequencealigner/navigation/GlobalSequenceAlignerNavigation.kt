package hr.fer.sequencealigner.navigation

import android.content.Context
import android.content.Intent
import hr.fer.navigation.NavigationDirections
import hr.fer.sequencealigner.R
import hr.fer.sequencealigner.ui.SequenceAlignerActivity

class GlobalSequenceAlignerNavigation(private val context: Context) : NavigationDirections {

	override fun getFeatureName(): String = "Needleman-Wunsch global alignment"
	override fun getFeatureIcon(): Int = R.drawable.ic_dna
	override fun getNavigationIntent(): Intent = Intent(context, SequenceAlignerActivity::class.java)

	override fun navigateToFeature() {
		val intent = getNavigationIntent()
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		context.startActivity(intent)
	}
}