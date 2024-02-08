package hr.fer.common.models

import androidx.navigation.NavDirections
import androidx.navigation.Navigator

sealed class NavigationCommand {
	data class ToDirection(val directions: NavDirections, val extras: Navigator.Extras?) : NavigationCommand()
	object Back : NavigationCommand()
}