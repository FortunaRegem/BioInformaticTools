package hr.fer.ukkonenstree.ui.result.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import hr.fer.common.base.BaseFragment
import hr.fer.ukkonenstree.BR
import hr.fer.ukkonenstree.R
import hr.fer.ukkonenstree.databinding.FragmentTreeResultBinding
import hr.fer.ukkonenstree.ui.result.viewmodel.TreeResultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TreeResultFragment : BaseFragment<FragmentTreeResultBinding, TreeResultViewModel>(
	BR.viewModel
) {

	override val layoutId: Int = R.layout.fragment_tree_result
	private val navArgs: TreeResultFragmentArgs by navArgs()
	override val viewModel: TreeResultViewModel by viewModel{ parametersOf(navArgs.sequence) }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initListeners()
	}

	private var isNextPressed = false
	private var isBackPressed = false
	private var handler = Handler(Looper.getMainLooper())

	private fun initListeners() {
		binding.buttonBack.setOnClickListener {
			binding.treeView.previousStep()
			isBackPressed = true
			handler.postDelayed({
				isBackPressed = false
				viewModel.activeNode.postValue(binding.treeView.activeNode)
				viewModel.activeEdge.postValue(binding.treeView.activeEdge)
				viewModel.activeLength.postValue(binding.treeView.activeLength)
				viewModel.reminder.postValue(binding.treeView.reminder)
			}, 11)
		}
		binding.buttonNext.setOnClickListener {
			binding.treeView.nextStep()
			isNextPressed = true
			handler.postDelayed({
				isNextPressed = false
				viewModel.activeNode.postValue(binding.treeView.activeNode)
				viewModel.activeEdge.postValue(binding.treeView.activeEdge)
				viewModel.activeLength.postValue(binding.treeView.activeLength)
				viewModel.reminder.postValue(binding.treeView.reminder)

			}, 11)
		}
	}

	override fun onStop() {
		handler.removeCallbacksAndMessages(null)
		super.onStop()
	}


}