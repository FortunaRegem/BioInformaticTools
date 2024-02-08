package hr.fer.sequencealigner.ui.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fer.common.extensions.snack
import hr.fer.sequencealigner.R
import hr.fer.sequencealigner.databinding.FragmentHistoryBinding
import hr.fer.sequencealigner.ui.history.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

	private val viewModel: HistoryViewModel by viewModel()
	private lateinit var binding: FragmentHistoryBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentHistoryBinding>(inflater, R.layout.fragment_history, container, false)
			.apply {
				viewModel = this@HistoryFragment.viewModel
				fragment = this@HistoryFragment
				lifecycleOwner = this@HistoryFragment
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
			navigateToAligner(it.tableParameters.sequenceA, it.tableParameters.sequenceB)
		}
	}

	private fun navigateToAligner(sequenceA: String, sequenceB: String) {
		findNavController().navigate(
			HistoryFragmentDirections.actionHistoryFragmentToAlignerFragment(
				sequenceA,
				sequenceB
			)
		)
	}

}