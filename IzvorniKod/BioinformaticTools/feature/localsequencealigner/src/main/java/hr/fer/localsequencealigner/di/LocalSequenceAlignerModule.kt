package hr.fer.localsequencealigner.di

import hr.fer.navigation.DirectionNamingEnum
import hr.fer.navigation.NavigationDirections
import hr.fer.localsequencealigner.navigation.LocalSequenceAlignerNavigation
import hr.fer.localsequencealigner.ui.aligner.view.LocalResultBottomSheetPicker
import hr.fer.localsequencealigner.ui.aligner.viewmodel.LocalAlignerViewModel
import hr.fer.localsequencealigner.ui.aligner.viewmodel.LocalResultViewModel
import hr.fer.localsequencealigner.ui.aligner.viewmodel.LocalTableViewModel
import hr.fer.localsequencealigner.ui.history.viewmodel.LocalHistoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LocalSequenceAlignerModule {
	fun getSequenceAlignerRoute() = sequenceAlignerRoute

	private var sequenceAlignerRoute = module {
		factory<NavigationDirections>(named(DirectionNamingEnum.LOCAL_SEQUENCE_ALIGNER.name)) {
			LocalSequenceAlignerNavigation(androidContext())
		}
	}

	private fun getDynamicProgrammingModules() = arrayListOf(
		viewModelModule,
		bottomSheetModule
	)
	internal fun inject() = loadDynamicProgrammingModules

	internal fun unload() = unloadDynamicProgrammingModules

	private val loadDynamicProgrammingModules by lazy {
		GlobalContext.loadKoinModules(getDynamicProgrammingModules())
	}

	private val unloadDynamicProgrammingModules by lazy {
		unloadKoinModules(getDynamicProgrammingModules())
	}

	private val viewModelModule = module {
		viewModel {
			LocalAlignerViewModel(
				alignUseCase = get()
			)
		}
		viewModel {
			LocalResultViewModel(
				alignUseCase = get()
			)
		}
		viewModel {
			LocalHistoryViewModel(
				historyUseCase = get()
			)
		}
		viewModel { (sequenceA: String, sequenceB: String) ->
			LocalTableViewModel(
				sequenceA = sequenceA,
				sequenceB = sequenceB,
				alignUseCase = get()
			)
		}
	}

	private val bottomSheetModule = module {
		factory {
			LocalResultBottomSheetPicker()
		}
	}
}