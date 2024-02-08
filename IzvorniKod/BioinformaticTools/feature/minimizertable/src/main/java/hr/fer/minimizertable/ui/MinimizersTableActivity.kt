package hr.fer.minimizertable.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import hr.fer.minimizertable.R
import hr.fer.minimizertable.databinding.ActivityMinimizersTableBinding
import hr.fer.minimizertable.di.MinimizerTableModule

class MinimizersTableActivity : AppCompatActivity() {

	lateinit var binding: ActivityMinimizersTableBinding
	private var navController: NavController? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		MinimizerTableModule.unload()
		MinimizerTableModule.inject()
		binding = DataBindingUtil.setContentView(this, R.layout.activity_minimizers_table)
	}

	override fun onDestroy() {
		MinimizerTableModule.unload()
		super.onDestroy()
	}

	override fun onStart() {
		super.onStart()

		initNavigationController()
	}

	private fun initNavigationController() {
		navController = findNavController(R.id.nav_host_fragment)
		navController!!.addOnDestinationChangedListener { _, destination, _ ->

		}
	}
}