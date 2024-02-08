package hr.fer.minimizertable.ui.result.view

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import hr.fer.common.base.BaseFragment
import hr.fer.minimizertable.BR
import hr.fer.minimizertable.R
import hr.fer.minimizertable.databinding.FragmentResultBinding
import hr.fer.minimizertable.ui.result.viewmodel.ResultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ResultFragment : BaseFragment<FragmentResultBinding, ResultViewModel>(
	BR.viewModel
) {

	override val layoutId: Int = R.layout.fragment_result
	private val navArgs: ResultFragmentArgs by navArgs()
	override val viewModel: ResultViewModel by viewModel{ parametersOf(navArgs.sequence, navArgs.w, navArgs.k) }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initListeners()
	}

	private fun initListeners() {
		binding.buttonBack.setOnClickListener {
			binding.tableView.previousStep()
			binding.buttonPlay.icon = ContextCompat.getDrawable(requireContext(), hr.fer.common.R.drawable.ic_play)
		}
		binding.buttonPlay.setOnClickListener {
			val isPlaying = binding.tableView.togglePlay()
			if (isPlaying) {
				binding.buttonPlay.icon = ContextCompat.getDrawable(requireContext(), hr.fer.common.R.drawable.ic_pause)
			} else {
				binding.buttonPlay.icon = ContextCompat.getDrawable(requireContext(), hr.fer.common.R.drawable.ic_play)
			}
		}
		binding.buttonNext.setOnClickListener {
			binding.tableView.nextStep()
			binding.buttonPlay.icon = ContextCompat.getDrawable(requireContext(), hr.fer.common.R.drawable.ic_play)
		}

		binding.buttonEnd.setOnClickListener {
			binding.tableView.jumpToEnd()
			binding.buttonPlay.icon = ContextCompat.getDrawable(requireContext(), hr.fer.common.R.drawable.ic_play)
		}
	}
}