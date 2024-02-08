package hr.fer.sequencealigner.ui.aligner.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hr.fer.common.extensions.snack
import hr.fer.sequencealigner.R
import hr.fer.sequencealigner.databinding.FragmentAlignerBinding
import hr.fer.sequencealigner.ui.aligner.viewmodel.AlignerViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlignerFragment : Fragment() {

	private val viewModel: AlignerViewModel by viewModel()
	private lateinit var binding: FragmentAlignerBinding
	private val args: AlignerFragmentArgs by navArgs()

	private val resultPicker: ResultBottomSheetPicker by inject()


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<FragmentAlignerBinding>(inflater, R.layout.fragment_aligner, container, false)
			.apply {
				viewModel = this@AlignerFragment.viewModel
				lifecycleOwner = this@AlignerFragment
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
			AlignerFragmentDirections.actionAlignerFragmentToHistoryFragment()
		)
	}

}