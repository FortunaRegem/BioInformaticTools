package hr.fer.localsequencealigner.ui.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fer.common.extensions.snack
import hr.fer.localsequencealigner.R
import hr.fer.localsequencealigner.databinding.FragmentHistoryLocalBinding
import hr.fer.localsequencealigner.ui.history.viewmodel.LocalHistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocalHistoryFragment : Fragment() {

	private val viewModel: LocalHistoryViewModel by viewModel()
	private lateinit var binding: FragmentHistoryLocalBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentHistoryLocalBinding>(inflater, R.layout.fragment_history_local, container, false)
			.apply {
				viewModel = this@LocalHistoryFragment.viewModel
				fragment = this@LocalHistoryFragment
				lifecycleOwner = this@LocalHistoryFragment
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
			LocalHistoryFragmentDirections.actionHistoryFragmentToAlignerFragment(
				sequenceA,
				sequenceB
			)
		)
	}

}