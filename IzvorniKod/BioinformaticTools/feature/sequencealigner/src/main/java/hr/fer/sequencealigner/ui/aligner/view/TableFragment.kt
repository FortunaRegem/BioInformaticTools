package hr.fer.sequencealigner.ui.aligner.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.navArgs
import hr.fer.common.base.BaseFragment
import hr.fer.sequencealigner.R
import hr.fer.sequencealigner.BR
import hr.fer.sequencealigner.databinding.FragmentTableBinding
import hr.fer.sequencealigner.ui.aligner.viewmodel.TableViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TableFragment : BaseFragment<FragmentTableBinding, TableViewModel>(
	BR.viewModel
) {

	override val layoutId: Int = R.layout.fragment_table
	private val navArgs: TableFragmentArgs by navArgs()
	override val viewModel: TableViewModel by viewModel{ parametersOf(navArgs.sequenceA, navArgs.sequenceB) }

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