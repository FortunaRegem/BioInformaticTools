package hr.fer.minimizertable.di

import hr.fer.navigation.DirectionNamingEnum
import hr.fer.navigation.NavigationDirections
import hr.fer.minimizertable.navigation.MinimizerTableNavigation
import hr.fer.minimizertable.ui.minimizers.viewmodel.MinimizersViewModel
import hr.fer.minimizertable.ui.result.viewmodel.ResultViewModel
import hr.fer.minimizertable.ui.history.viewmodel.HistoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

object MinimizerTableModule {
	fun getMinimizerTableRoute() = minimizerTableRoute

	private var minimizerTableRoute = module {
		factory<NavigationDirections>(named(DirectionNamingEnum.MINIMIZER_TABLE.name)) {
			MinimizerTableNavigation(androidContext())
		}
	}

	private fun getMinimizerTableModules() = arrayListOf(
		viewModelModule
	)
	internal fun inject() = loadMinimizerTableModules

	internal fun unload() = unloadMinimizerTableModules

	private val loadMinimizerTableModules by lazy {
		GlobalContext.loadKoinModules(getMinimizerTableModules())
	}

	private val unloadMinimizerTableModules by lazy {
		unloadKoinModules(getMinimizerTableModules())
	}

	private val viewModelModule = module {
		viewModel {
			MinimizersViewModel(
				minimizerUseCase = get()
			)
		}
		viewModel { (sequence: String, w: Int, k: Int) ->
			ResultViewModel(
				sequence = sequence,
				w = w,
				k = k,
				minimizerUseCase = get()
			)
		}
		viewModel {
			HistoryViewModel(
				historyUseCase = get()
			)
		}
	}
}