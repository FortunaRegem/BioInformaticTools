package hr.fer.minimizers.di

import hr.fer.minimizers.data.factory.MinimizersCalculatorFactory
import hr.fer.minimizers.data.repository.MinimizersRepository
import hr.fer.minimizers.domain.contract.MinimizersCalculatorFactoryContract
import hr.fer.minimizers.domain.contract.MinimizersRepositoryContract
import hr.fer.minimizers.domain.usecase.MinimizersUseCase
import hr.fer.minimizertable.ui.history.contract.HistoryUseCaseContract
import hr.fer.minimizertable.ui.minimizers.contract.MinimizerTableUseCaseContract
import hr.fer.minimizertable.ui.result.contract.MinimizerTableResultUseCaseContract
import org.koin.core.context.GlobalContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object MinimizersModule {
	private fun getMinimizersModules() = arrayListOf(
		useCaseModule,
		factoryModule,
		repositoryModule
	)

	fun inject() = loadMinimizersModules

	fun unload() = unloadMinimizersModules

	private val loadMinimizersModules by lazy {
		GlobalContext.loadKoinModules(getMinimizersModules())
	}

	private val unloadMinimizersModules by lazy {
		unloadKoinModules(getMinimizersModules())
	}

	private val useCaseModule: Module = module {
		factory<MinimizerTableUseCaseContract> { MinimizersUseCase(
			minimizersRepository = get(),
			minimizersCalculatorFactory = get()
		) }

		factory<MinimizerTableResultUseCaseContract> { MinimizersUseCase(
			minimizersRepository = get(),
			minimizersCalculatorFactory = get()
		) }

		factory<HistoryUseCaseContract> { MinimizersUseCase(
			minimizersRepository = get(),
			minimizersCalculatorFactory = get()
		) }
	}

	private val repositoryModule: Module = module {
		single<MinimizersRepositoryContract> { MinimizersRepository(
			minimizersDao = get()
		) }
	}

	private val factoryModule: Module = module {
		factory<MinimizersCalculatorFactoryContract> { MinimizersCalculatorFactory() }
	}
}