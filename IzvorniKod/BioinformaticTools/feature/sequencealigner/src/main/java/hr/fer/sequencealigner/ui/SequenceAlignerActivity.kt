package hr.fer.sequencealigner.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import hr.fer.sequencealigner.R
import hr.fer.sequencealigner.databinding.ActivitySequenceAlignerBinding
import hr.fer.sequencealigner.di.GlobalSequenceAlignerModule

class SequenceAlignerActivity : AppCompatActivity() {

	lateinit var binding: ActivitySequenceAlignerBinding
	private var navController: NavController? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		GlobalSequenceAlignerModule.unload()
		GlobalSequenceAlignerModule.inject()
		binding = DataBindingUtil.setContentView(this, R.layout.activity_sequence_aligner)
	}

	override fun onDestroy() {
		GlobalSequenceAlignerModule.unload()
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