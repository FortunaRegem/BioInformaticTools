package hr.fer.fmindextable.di

import hr.fer.fmindextable.navigation.FMIndexTableNavigation
import hr.fer.fmindextable.ui.fmindex.viewmodel.FMIndexViewModel
import hr.fer.fmindextable.ui.history.viewmodel.HistoryViewModel
import hr.fer.fmindextable.ui.result.viewmodel.ResultViewModel
import hr.fer.navigation.DirectionNamingEnum
import hr.fer.navigation.NavigationDirections
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

object FMIndexTableModule {
	fun getFMIndexTableRoute() = fmIndexTableRoute

	private var fmIndexTableRoute = module {
		factory<NavigationDirections>(named(DirectionNamingEnum.FM_INDEX_TABLE.name)) {
			FMIndexTableNavigation(androidContext())
		}
	}

	private fun getFMIndexTableModules() = arrayListOf(
		viewModelModule
	)
	internal fun inject() = loadFMIndexTableModules

	internal fun unload() = unloadFMIndexTableModules

	private val loadFMIndexTableModules by lazy {
		GlobalContext.loadKoinModules(getFMIndexTableModules())
	}

	private val unloadFMIndexTableModules by lazy {
		unloadKoinModules(getFMIndexTableModules())
	}

	private val viewModelModule = module {
		viewModel {
			FMIndexViewModel(
				fmIndexUseCase = get()
			)
		}
		viewModel { (sequence: String, pattern: String) ->
			ResultViewModel(
				sequence = sequence,
				pattern = pattern,
				fmIndexUseCase = get()
			)
		}
		viewModel {
			HistoryViewModel(
				historyUseCase = get()
			)
		}
	}
}