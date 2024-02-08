package hr.fer.bioinformatictools.di.modules

import hr.fer.bioinformatictools.ui.viewmodel.SelectFeatureViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {

	fun getViewModelModule() = viewModelModule

	private val viewModelModule = module {
		viewModel {
			SelectFeatureViewModel()
		}
	}
}