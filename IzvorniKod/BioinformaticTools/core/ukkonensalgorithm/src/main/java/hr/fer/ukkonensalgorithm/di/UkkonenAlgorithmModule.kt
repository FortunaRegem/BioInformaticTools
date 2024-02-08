package hr.fer.ukkonensalgorithm.di

import hr.fer.ukkonensalgorithm.data.factory.UkkonenTreeFactory
import hr.fer.ukkonensalgorithm.domain.contract.UkkonenCalculatorFactoryContract
import hr.fer.ukkonensalgorithm.domain.usecase.UkkonenUseCase
import hr.fer.ukkonenstree.ui.result.contract.UkkonensTreeResultUseCaseContract
import org.koin.core.context.GlobalContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object UkkonenAlgorithmModule {
	private fun getUkkonenModules() = arrayListOf(
		useCaseModule,
		factoryModule,
		repositoryModule
	)

	fun inject() = loadUkkonenModules

	fun unload() = unloadUkkonenModules

	private val loadUkkonenModules by lazy {
		GlobalContext.loadKoinModules(getUkkonenModules())
	}

	private val unloadUkkonenModules by lazy {
		unloadKoinModules(getUkkonenModules())
	}

	private val useCaseModule: Module = module {
		factory<UkkonensTreeResultUseCaseContract> { UkkonenUseCase(
//			fmIndexRepository = get(),
			fmIndexCalculatorFactory = get()
		) }
//
//		factory<FmIndexTableResultUseCaseContract> { UkkonenUseCase(
//			fmIndexRepository = get(),
//			fmIndexCalculatorFactory = get()
//		) }
//
//		factory<HistoryUseCaseContract> { UkkonenUseCase(
//			fmIndexRepository = get(),
//			fmIndexCalculatorFactory = get()
//		) }
	}

	private val repositoryModule: Module = module {
//		single<UkkonenRepositoryContract> { UkkonenRepository(
//			fmIndexDao = get()
//		) }
	}

	private val factoryModule: Module = module {
		factory<UkkonenCalculatorFactoryContract> { UkkonenTreeFactory() }
	}
}