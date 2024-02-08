package hr.fer.bioinformatictools.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import hr.fer.bioinformatictools.R
import hr.fer.bioinformatictools.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	lateinit var binding: ActivityMainBinding
	private var navController: NavController? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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