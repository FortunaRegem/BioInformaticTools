package hr.fer.ukkonenstree.ui.ukkonenstree.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fer.common.extensions.snack
import hr.fer.ukkonenstree.R
import hr.fer.ukkonenstree.databinding.FragmentUkkonensTreeBinding
import hr.fer.ukkonenstree.ui.ukkonenstree.viewmodel.UkkonensTreeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UkkonensTreeFragment : Fragment() {

	private val viewModel: UkkonensTreeViewModel by viewModel()
	private lateinit var binding: FragmentUkkonensTreeBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentUkkonensTreeBinding>(inflater, R.layout.fragment_ukkonens_tree, container, false)
			.apply {
				viewModel = this@UkkonensTreeFragment.viewModel
				lifecycleOwner = this@UkkonensTreeFragment
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
			findNavController().navigate(
				UkkonensTreeFragmentDirections.actionUkkonensTreeFragmentToTreeResultFragment(
					"${viewModel.sequence.value!!}$"
				)
			)
		}
//		binding.buttonHistory.setOnClickListener {
//			navigateToHistory()
//		}
	}

	private fun initObservers() {
		viewModel.errorMessage.observe(viewLifecycleOwner) {
			if (it.isNotEmpty()) {
				snack(it).show()
			}
		}
	}

	private fun navigateToHistory() {
//		findNavController().navigate(
//			UkkonensTreeFragmentDirections.actionMinimizersFragmentToHistoryFragment()
//		)
	}

}