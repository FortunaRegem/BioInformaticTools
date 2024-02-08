package hr.fer.ukkonenstree.ui.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fer.common.extensions.snack
import hr.fer.ukkonenstree.R
import hr.fer.ukkonenstree.databinding.FragmentUkkonensTreeHistoryBinding
import hr.fer.ukkonenstree.ui.history.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UkkonensTreeHistoryFragment : Fragment() {

	private val viewModel: HistoryViewModel by viewModel()
	private lateinit var binding: FragmentUkkonensTreeHistoryBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentUkkonensTreeHistoryBinding>(inflater, R.layout.fragment_ukkonens_tree_history, container, false)
			.apply {
				viewModel = this@UkkonensTreeHistoryFragment.viewModel
				fragment = this@UkkonensTreeHistoryFragment
				lifecycleOwner = this@UkkonensTreeHistoryFragment
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

//		viewModel.itemClicked.observe(viewLifecycleOwner) {
//			navigateToAligner(it.sequence, it.w, it.k)
//		}
	}

//	private fun navigateToAligner(sequence: String, w: Int, k: Int) {
//		findNavController().navigate(
//			MinimizersHistoryFragmentDirections.actionHistoryFragmentToResultFragment(
//				sequence,
//				w,
//				k
//			)
//		)
//	}

}