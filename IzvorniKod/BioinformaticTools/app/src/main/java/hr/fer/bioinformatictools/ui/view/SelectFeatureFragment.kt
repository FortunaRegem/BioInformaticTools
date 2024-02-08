package hr.fer.bioinformatictools.ui.view

import hr.fer.bioinformatictools.BR
import hr.fer.bioinformatictools.R
import hr.fer.bioinformatictools.databinding.FragmentSelectFeatureBinding
import hr.fer.bioinformatictools.ui.viewmodel.SelectFeatureViewModel
import hr.fer.common.base.BaseFragment
import hr.fer.common.models.BindableItem
import hr.fer.navigation.NavigationDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectFeatureFragment : BaseFragment<FragmentSelectFeatureBinding, SelectFeatureViewModel>(
	bindViewModelBR = BR.viewModel,
	bindFragmentBR = BR.fragment
) {

	override val layoutId: Int = R.layout.fragment_select_feature
	override val viewModel: SelectFeatureViewModel by viewModel()
}

fun NavigationDirections.toBindableItem() = BindableItem(
	data = this,
	variableId = BR.featureItem,
	layoutId = R.layout.item_feature
)