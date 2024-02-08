package hr.fer.minimizertable.navigation

import android.content.Context
import android.content.Intent
import hr.fer.minimizertable.R
import hr.fer.minimizertable.ui.MinimizersTableActivity
import hr.fer.navigation.NavigationDirections

class MinimizerTableNavigation(private val context: Context) : NavigationDirections {

	override fun getFeatureName(): String = "Sequence minimizers"
	override fun getFeatureIcon(): Int = R.drawable.ic_dna
	override fun getNavigationIntent(): Intent = Intent(context, MinimizersTableActivity::class.java)

	override fun navigateToFeature() {
		val intent = getNavigationIntent()
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		context.startActivity(intent)
	}
}