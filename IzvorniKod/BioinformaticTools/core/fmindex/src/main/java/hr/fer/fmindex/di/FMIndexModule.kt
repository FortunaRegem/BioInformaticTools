package hr.fer.fmindex.di

import hr.fer.fmindex.data.factory.FMIndexCalculatorFactory
import hr.fer.fmindex.data.repository.FMIndexRepository
import hr.fer.fmindex.domain.contract.FMIndexCalculatorFactoryContract
import hr.fer.fmindex.domain.contract.FMIndexRepositoryContract
import hr.fer.fmindex.domain.usecase.FMIndexUseCase
import hr.fer.fmindextable.ui.fmindex.contract.FMIndexTableUseCaseContract
import hr.fer.fmindextable.ui.history.contract.HistoryUseCaseContract
import hr.fer.fmindextable.ui.result.contract.FmIndexTableResultUseCaseContract
import org.koin.core.context.GlobalContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object FMIndexModule {
	private fun getFMIndexModules() = arrayListOf(
		useCaseModule,
		factoryModule,
		repositoryModule
	)

	fun inject() = loadFMIndexModules

	fun unload() = unloadFMIndexModules

	private val loadFMIndexModules by lazy {
		GlobalContext.loadKoinModules(getFMIndexModules())
	}

	private val unloadFMIndexModules by lazy {
		unloadKoinModules(getFMIndexModules())
	}

	private val useCaseModule: Module = module {
		factory<FMIndexTableUseCaseContract> { FMIndexUseCase(
			fmIndexRepository = get(),
			fmIndexCalculatorFactory = get()
		) }

		factory<FmIndexTableResultUseCaseContract> { FMIndexUseCase(
			fmIndexRepository = get(),
			fmIndexCalculatorFactory = get()
		) }

		factory<HistoryUseCaseContract> { FMIndexUseCase(
			fmIndexRepository = get(),
			fmIndexCalculatorFactory = get()
		) }
	}

	private val repositoryModule: Module = module {
		single<FMIndexRepositoryContract> { FMIndexRepository(
			fmIndexDao = get()
		) }
	}

	private val factoryModule: Module = module {
		factory<FMIndexCalculatorFactoryContract> { FMIndexCalculatorFactory() }
	}
}