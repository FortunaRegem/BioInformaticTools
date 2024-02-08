package hr.fer.fmindextable.ui.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fer.common.extensions.snack
import hr.fer.fmindextable.R
import hr.fer.fmindextable.databinding.FragmentFmindexHistoryBinding
import hr.fer.fmindextable.ui.history.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FMIndexHistoryFragment : Fragment() {

	private val viewModel: HistoryViewModel by viewModel()
	private lateinit var binding: FragmentFmindexHistoryBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentFmindexHistoryBinding>(inflater, R.layout.fragment_fmindex_history, container, false)
			.apply {
				viewModel = this@FMIndexHistoryFragment.viewModel
				fragment = this@FMIndexHistoryFragment
				lifecycleOwner = this@FMIndexHistoryFragment
			}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initListeners()
		initObservers()
	}

	private fun initListeners() {
		binding.arrowBack.setOnClickListener {
			findNavController().popBackStack()
		}
	}

	private fun initObservers() {
		viewModel.errorMessage.observe(viewLifecycleOwner) {
			if (it.isNotEmpty()) {
				snack(it).show()
			}
		}

		viewModel.itemClicked.observe(viewLifecycleOwner) {
			navigateToAligner(it.sequence, it.pattern)
		}
	}

	private fun navigateToAligner(sequence: String, pattern: String) {
		findNavController().navigate(
			FMIndexHistoryFragmentDirections.actionHistoryFragmentToResultFragment(
				sequence,
				pattern
			)
		)
	}

}