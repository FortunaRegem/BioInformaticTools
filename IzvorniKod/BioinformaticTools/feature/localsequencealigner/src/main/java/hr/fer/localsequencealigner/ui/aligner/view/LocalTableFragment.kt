package hr.fer.localsequencealigner.ui.aligner.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.navArgs
import hr.fer.common.base.BaseFragment
import hr.fer.localsequencealigner.R
import hr.fer.localsequencealigner.BR
import hr.fer.localsequencealigner.databinding.FragmentTableLocalBinding
import hr.fer.localsequencealigner.ui.aligner.viewmodel.LocalTableViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LocalTableFragment : BaseFragment<FragmentTableLocalBinding, LocalTableViewModel>(
	BR.viewModel
) {

	override val layoutId: Int = R.layout.fragment_table_local
	private val navArgs: LocalTableFragmentArgs by navArgs()
	override val viewModel: LocalTableViewModel by viewModel{ parametersOf(navArgs.sequenceA, navArgs.sequenceB) }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initListeners()
		initObservers()
	}

	private fun initListeners() {
		binding.buttonBack.setOnClickListener {
			binding.table.moveMainPathBack()
			viewModel.currentResult.postValue(binding.table.currentMainPathData)
		}
		binding.buttonNext.setOnClickListener {
			binding.table.moveMainPathForward()
			viewModel.currentResult.postValue(binding.table.currentMainPathData)
		}
	}

	private fun initObservers() {
		viewModel.dataHasBeenSet.observe(viewLifecycleOwner) {
			Handler(Looper.getMainLooper()).postDelayed({
				viewModel.currentResult.postValue(binding.table.currentMainPathData)
			}, 100)
		}
	}

}