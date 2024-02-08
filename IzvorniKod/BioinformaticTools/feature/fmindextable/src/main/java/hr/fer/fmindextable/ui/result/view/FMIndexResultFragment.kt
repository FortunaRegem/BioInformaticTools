package hr.fer.fmindextable.ui.result.view

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import hr.fer.common.base.BaseFragment
import hr.fer.fmindextable.BR
import hr.fer.fmindextable.R
import hr.fer.fmindextable.databinding.FragmentFmindexResultBinding
import hr.fer.fmindextable.ui.result.viewmodel.ResultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FMIndexResultFragment : BaseFragment<FragmentFmindexResultBinding, ResultViewModel>(
	BR.viewModel
) {

	override val layoutId: Int = R.layout.fragment_fmindex_result
	private val navArgs: FMIndexResultFragmentArgs by navArgs()
	override val viewModel: ResultViewModel by viewModel{ parametersOf(navArgs.sequence, navArgs.pattern) }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initListeners()
	}

	private fun initListeners() {
		binding.buttonBack.setOnClickListener {
			binding.tableView.previousStep()
			viewModel.currentStepDescription.postValue(binding.tableView.getText())
		}
		binding.buttonNext.setOnClickListener {
			binding.tableView.nextStep()
			viewModel.currentStepDescription.postValue(binding.tableView.getText())
		}

	}
}