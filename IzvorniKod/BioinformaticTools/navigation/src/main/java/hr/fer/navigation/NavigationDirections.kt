package hr.fer.navigation

import android.content.Intent

interface NavigationDirections {
	fun getFeatureName(): String
	fun getFeatureIcon(): Int
	fun getNavigationIntent(): Intent

	fun navigateToFeature()
}