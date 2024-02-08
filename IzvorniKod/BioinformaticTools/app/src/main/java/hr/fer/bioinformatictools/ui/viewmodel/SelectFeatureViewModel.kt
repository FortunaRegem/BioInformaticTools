package hr.fer.bioinformatictools.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.fer.bioinformatictools.ui.view.toBindableItem
import hr.fer.common.base.BaseViewModel
import hr.fer.common.models.BindableItem
import hr.fer.navigation.DirectionNamingEnum
import hr.fer.navigation.NavigationDirections
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

class SelectFeatureViewModel : BaseViewModel() {

	val features: MutableLiveData<ArrayList<BindableItem?>> = MutableLiveData()

	init {
		getFeatures()
	}

	private fun getFeatures() {
		features.postValue(
			DirectionNamingEnum.values().map { feature ->
				getKoin().getOrNull<NavigationDirections>(named(feature.name))?.toBindableItem()
			} as ArrayList<BindableItem?>
		)
	}
}