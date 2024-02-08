package hr.fer.fmindextable.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import hr.fer.fmindextable.R
import hr.fer.fmindextable.databinding.ActivityFmindexTableBinding
import hr.fer.fmindextable.di.FMIndexTableModule

class FMIndexTableActivity : AppCompatActivity() {

	lateinit var binding: ActivityFmindexTableBinding
	private var navController: NavController? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		FMIndexTableModule.unload()
		FMIndexTableModule.inject()
		binding = DataBindingUtil.setContentView(this, R.layout.activity_fmindex_table)
	}

	override fun onDestroy() {
		FMIndexTableModule.unload()
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