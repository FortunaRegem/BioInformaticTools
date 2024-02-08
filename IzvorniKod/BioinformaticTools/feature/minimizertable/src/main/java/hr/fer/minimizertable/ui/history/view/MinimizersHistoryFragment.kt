package hr.fer.minimizertable.ui.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fer.common.extensions.snack
import hr.fer.minimizertable.R
import hr.fer.minimizertable.databinding.FragmentMinimizersHistoryBinding
import hr.fer.minimizertable.ui.history.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MinimizersHistoryFragment : Fragment() {

	private val viewModel: HistoryViewModel by viewModel()
	private lateinit var binding: FragmentMinimizersHistoryBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentMinimizersHistoryBinding>(inflater, R.layout.fragment_minimizers_history, container, false)
			.apply {
				viewModel = this@MinimizersHistoryFragment.viewModel
				fragment = this@MinimizersHistoryFragment
				lifecycleOwner = this@MinimizersHistoryFragment
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
			navigateToAligner(it.sequence, it.w, it.k)
		}
	}

	private fun navigateToAligner(sequence: String, w: Int, k: Int) {
		findNavController().navigate(
			MinimizersHistoryFragmentDirections.actionHistoryFragmentToResultFragment(
				sequence,
				w,
				k
			)
		)
	}

}