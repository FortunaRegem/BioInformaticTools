package hr.fer.ukkonenstree.di

import hr.fer.navigation.DirectionNamingEnum
import hr.fer.navigation.NavigationDirections
import hr.fer.ukkonenstree.navigation.UkkonensTreeNavigation
import hr.fer.ukkonenstree.ui.result.viewmodel.TreeResultViewModel
import hr.fer.ukkonenstree.ui.ukkonenstree.viewmodel.UkkonensTreeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

object UkkonensTreeModule {
	fun getUkkonensTreeRoute() = ukkonensTreeRoute

	private var ukkonensTreeRoute = module {
		factory<NavigationDirections>(named(DirectionNamingEnum.UKKONENS_TREE.name)) {
			UkkonensTreeNavigation(androidContext())
		}
	}

	private fun getUkkonensTreeModules() = arrayListOf(
		viewModelModule
	)
	internal fun inject() = loadUkkonensTreeModules

	internal fun unload() = unloadUkkonensTreeModules

	private val loadUkkonensTreeModules by lazy {
		GlobalContext.loadKoinModules(getUkkonensTreeModules())
	}

	private val unloadUkkonensTreeModules by lazy {
		unloadKoinModules(getUkkonensTreeModules())
	}

	private val viewModelModule = module {
		viewModel {
			UkkonensTreeViewModel()
		}
		viewModel { (sequence: String) ->
			TreeResultViewModel(
				sequence = sequence,
				ukkonenUseCase = get()
			)
		}
//		viewModel {
//			HistoryViewModel(
//				historyUseCase = get()
//			)
//		}
	}
}