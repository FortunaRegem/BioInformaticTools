package hr.fer.sequencealigner.di

import hr.fer.navigation.DirectionNamingEnum
import hr.fer.navigation.NavigationDirections
import hr.fer.sequencealigner.navigation.GlobalSequenceAlignerNavigation
import hr.fer.sequencealigner.ui.aligner.view.ResultBottomSheetPicker
import hr.fer.sequencealigner.ui.aligner.viewmodel.AlignerViewModel
import hr.fer.sequencealigner.ui.aligner.viewmodel.ResultViewModel
import hr.fer.sequencealigner.ui.aligner.viewmodel.TableViewModel
import hr.fer.sequencealigner.ui.history.viewmodel.HistoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GlobalSequenceAlignerModule {
	fun getSequenceAlignerRoute() = sequenceAlignerRoute

	private var sequenceAlignerRoute = module {
		factory<NavigationDirections>(named(DirectionNamingEnum.GLOBAL_SEQUENCE_ALIGNER.name)) {
			GlobalSequenceAlignerNavigation(androidContext())
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
			AlignerViewModel(
				alignUseCase = get()
			)
		}
		viewModel {
			ResultViewModel(
				alignUseCase = get()
			)
		}
		viewModel {
			HistoryViewModel(
				historyUseCase = get()
			)
		}
		viewModel { (sequenceA: String, sequenceB: String) ->
			TableViewModel(
				sequenceA = sequenceA,
				sequenceB = sequenceB,
				alignUseCase = get()
			)
		}
	}

	private val bottomSheetModule = module {
		factory {
			ResultBottomSheetPicker()
		}
	}
}