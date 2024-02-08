package hr.fer.minimizertable.ui.minimizers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fer.common.extensions.snack
import hr.fer.minimizertable.R
import hr.fer.minimizertable.databinding.FragmentMinimizersBinding
import hr.fer.minimizertable.ui.minimizers.viewmodel.MinimizersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MinimizersFragment : Fragment() {

	private val viewModel: MinimizersViewModel by viewModel()
	private lateinit var binding: FragmentMinimizersBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentMinimizersBinding>(inflater, R.layout.fragment_minimizers, container, false)
			.apply {
				viewModel = this@MinimizersFragment.viewModel
				lifecycleOwner = this@MinimizersFragment
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
					MinimizersFragmentDirections.actionMinimizersFragmentToTableFragment(
						viewModel.sequence.value!!,
						viewModel.w.value!!.toInt(),
						viewModel.k.value!!.toInt()
					)
				)
			}
		}
	}

	private fun navigateToHistory() {
		findNavController().navigate(
			MinimizersFragmentDirections.actionMinimizersFragmentToHistoryFragment()
		)
	}

}