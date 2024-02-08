package hr.fer.fmindextable.ui.fmindex.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fer.common.extensions.snack
import hr.fer.fmindextable.R
import hr.fer.fmindextable.databinding.FragmentFmindexBinding
import hr.fer.fmindextable.ui.fmindex.viewmodel.FMIndexViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FMIndexFragment : Fragment() {

	private val viewModel: FMIndexViewModel by viewModel()
	private lateinit var binding: FragmentFmindexBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentFmindexBinding>(inflater, R.layout.fragment_fmindex, container, false)
			.apply {
				viewModel = this@FMIndexFragment.viewModel
				lifecycleOwner = this@FMIndexFragment
			}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initListeners()
		initObservers()
	}

	private fun initListeners() {
		binding.buttonAlign.setOnClickListener {
			viewModel.calculate()
		}
		binding.buttonHistory.setOnClickListener {
			navigateToHistory()
		}
	}

	private fun initObservers() {
		viewModel.errorMessage.observe(viewLifecycleOwner) {
			if (it.isNotEmpty()) {
				snack(it).show()
			}
		}
		viewModel.minimizingDone.observe(viewLifecycleOwner) {
			if (it) {
				findNavController().navigate(
					FMIndexFragmentDirections.actionFmindexFragmentToTableFragment(
						viewModel.sequence.value!!,
						viewModel.pattern.value!!
					)
				)
			}
		}
	}

	private fun navigateToHistory() {
		findNavController().navigate(
			FMIndexFragmentDirections.actionFmindexFragmentToHistoryFragment()
		)
	}

}