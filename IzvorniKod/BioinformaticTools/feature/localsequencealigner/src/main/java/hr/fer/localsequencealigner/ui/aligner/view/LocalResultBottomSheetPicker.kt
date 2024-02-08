package hr.fer.localsequencealigner.ui.aligner.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import hr.fer.common.base.BaseBottomSheet
import hr.fer.localsequencealigner.R
import hr.fer.localsequencealigner.databinding.BottomSheetLocalResultsBinding
import hr.fer.localsequencealigner.ui.aligner.viewmodel.LocalResultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocalResultBottomSheetPicker : BaseBottomSheet() {

	private var sequenceA: String? = null
	private var sequenceB: String? = null

	private val viewModel: LocalResultViewModel by viewModel()
	private lateinit var binding: BottomSheetLocalResultsBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate<BottomSheetLocalResultsBinding>(inflater, R.layout.bottom_sheet_local_results, container, false)
			.apply {
				viewModel = this@LocalResultBottomSheetPicker.viewModel
				fragment = this@LocalResultBottomSheetPicker
				lifecycleOwner = this@LocalResultBottomSheetPicker
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
		findNavController().navigate(LocalAlignerFragmentDirections.actionAlignerFragmentToTableFragment(sequenceA ?: "", sequenceB ?: ""))
	}

}