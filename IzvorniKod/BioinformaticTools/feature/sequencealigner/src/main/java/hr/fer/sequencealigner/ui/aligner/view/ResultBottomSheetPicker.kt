package hr.fer.sequencealigner.ui.aligner.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import hr.fer.common.base.BaseBottomSheet
import hr.fer.sequencealigner.R
import hr.fer.sequencealigner.databinding.BottomSheetResultsBinding
import hr.fer.sequencealigner.ui.aligner.viewmodel.ResultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultBottomSheetPicker : BaseBottomSheet() {

	private var sequenceA: String? = null
	private var sequenceB: String? = null

	private val viewModel: ResultViewModel by viewModel()
	private lateinit var binding: BottomSheetResultsBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<BottomSheetResultsBinding>(inflater, R.layout.bottom_sheet_results, container, false)
			.apply {
				viewModel = this@ResultBottomSheetPicker.viewModel
				fragment = this@ResultBottomSheetPicker
				lifecycleOwner = this@ResultBottomSheetPicker
			}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (sequenceA != null && sequenceB != null) {
			viewModel.getResults(sequenceA!!, sequenceB!!)
		}

		initListeners()
	}

	private fun initListeners() {
		binding.buttonTable.setOnClickListener {
			navigateToTable()
			dismiss()
		}
	}

	fun setData(sequenceA: String?, sequenceB: String?) {
		this.sequenceA = sequenceA
		this.sequenceB = sequenceB
	}

	private fun navigateToTable() {
		findNavController().navigate(AlignerFragmentDirections.actionAlignerFragmentToTableFragment(sequenceA ?: "", sequenceB ?: ""))
	}

}