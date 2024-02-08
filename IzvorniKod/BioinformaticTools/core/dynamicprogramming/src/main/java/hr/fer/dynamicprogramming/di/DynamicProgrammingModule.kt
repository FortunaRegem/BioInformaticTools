package hr.fer.dynamicprogramming.di

import hr.fer.dynamicprogramming.data.factory.MinimumDistanceTableFactory
import hr.fer.dynamicprogramming.data.repository.TableRepository
import hr.fer.dynamicprogramming.domain.contract.MinimumDistanceTableFactoryContract
import hr.fer.dynamicprogramming.domain.contract.TableRepositoryContract
import hr.fer.dynamicprogramming.domain.usecase.TableUseCase
import hr.fer.localsequencealigner.ui.aligner.contract.AlignLocalUseCaseContract
import hr.fer.localsequencealigner.ui.history.contract.HistoryLocalUseCaseContract
import hr.fer.sequencealigner.ui.aligner.contract.AlignUseCaseContract
import hr.fer.sequencealigner.ui.history.contract.HistoryUseCaseContract
import org.koin.core.context.GlobalContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DynamicProgrammingModule {

	private fun getDynamicProgrammingModules() = arrayListOf(
		useCaseModule,
		factoryModule,
		repositoryModule
	)

	fun inject() = loadDynamicProgrammingModules

	fun unload() = unloadDynamicProgrammingModules

	private val loadDynamicProgrammingModules by lazy {
		GlobalContext.loadKoinModules(getDynamicProgrammingModules())
	}

	private val unloadDynamicProgrammingModules by lazy {
		unloadKoinModules(getDynamicProgrammingModules())
	}

	private val useCaseModule: Module = module {
		factory<AlignUseCaseContract> { TableUseCase(
			tableRepository = get(),
			minimumDistanceTableFactory = get()
		) }

		factory<HistoryUseCaseContract> { TableUseCase(
			tableRepository = get(),
			minimumDistanceTableFactory = get()
		) }

		factory<AlignLocalUseCaseContract> { TableUseCase(
			tableRepository = get(),
			minimumDistanceTableFactory = get()
		) }

		factory<HistoryLocalUseCaseContract> { TableUseCase(
			tableRepository = get(),
			minimumDistanceTableFactory = get()
		) }
	}

	private val repositoryModule: Module = module {
		single<TableRepositoryContract> { TableRepository(
			dynamicProgrammingRoomDao = get()
		) }
	}

	private val factoryModule: Module = module {
		factory<MinimumDistanceTableFactoryContract> { MinimumDistanceTableFactory() }
	}
}