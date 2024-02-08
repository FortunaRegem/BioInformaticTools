package hr.fer.ukkonenstree.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import hr.fer.ukkonenstree.R
import hr.fer.ukkonenstree.databinding.ActivityUkkonensTreeBinding
import hr.fer.ukkonenstree.di.UkkonensTreeModule

class UkkonensTreeActivity : AppCompatActivity() {

	lateinit var binding: ActivityUkkonensTreeBinding
	private var navController: NavController? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		UkkonensTreeModule.unload()
		UkkonensTreeModule.inject()
		binding = DataBindingUtil.setContentView(this, R.layout.activity_ukkonens_tree)
	}

	override fun onDestroy() {
		UkkonensTreeModule.unload()
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