package hr.fer.localsequencealigner.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import hr.fer.localsequencealigner.R
import hr.fer.localsequencealigner.databinding.ActivityLocalSequenceAlignerBinding
import hr.fer.localsequencealigner.di.LocalSequenceAlignerModule

class LocalSequenceAlignerActivity : AppCompatActivity() {

	lateinit var binding: ActivityLocalSequenceAlignerBinding
	private var navController: NavController? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		LocalSequenceAlignerModule.unload()
		LocalSequenceAlignerModule.inject()
		binding = DataBindingUtil.setContentView(this, R.layout.activity_local_sequence_aligner)
	}

	override fun onDestroy() {
		LocalSequenceAlignerModule.unload()
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