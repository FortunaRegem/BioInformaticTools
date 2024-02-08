package hr.fer.localsequencealigner.ui.aligner.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hr.fer.common.extensions.snack
import hr.fer.localsequencealigner.R
import hr.fer.localsequencealigner.databinding.FragmentAlignerLocalBinding
import hr.fer.localsequencealigner.ui.aligner.viewmodel.LocalAlignerViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocalAlignerFragment : Fragment() {

	private val viewModel: LocalAlignerViewModel by viewModel()
	private lateinit var binding: FragmentAlignerLocalBinding
	private val args: LocalAlignerFragmentArgs by navArgs()

	private val resultPicker: LocalResultBottomSheetPicker by inject()


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentAlignerLocalBinding>(inflater, R.layout.fragment_aligner_local, container, false)
			.apply {
				viewModel = this@LocalAlignerFragment.viewModel
				lifecycleOwner = this@LocalAlignerFragment
			}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		if (args.sequenceA != null && args.sequenceB != null) {
			viewModel.initData(args.sequenceA!!, args.sequenceB!!)
		}

		initListeners()
		initObservers()
	}

	private fun initListeners() {
		binding.buttonAlign.setOnClickListener {
			viewModel.match()
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
		viewModel.alignmentDone.observe(viewLifecycleOwner) {
			if (it) {
				resultPicker.setData(viewModel.sequenceA.value, viewModel.sequenceB.value)
				if(!resultPicker.isVisible) {
					resultPicker.show(requireActivity().supportFragmentManager, "sport")
				}
			}
		}
	}

	private fun navigateToHistory() {
		findNavController().navigate(
			LocalAlignerFragmentDirections.actionAlignerFragmentToHistoryFragment()
		)
	}

}