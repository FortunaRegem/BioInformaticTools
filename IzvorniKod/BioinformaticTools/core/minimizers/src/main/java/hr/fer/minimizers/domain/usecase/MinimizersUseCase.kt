package hr.fer.minimizers.domain.usecase

import hr.fer.minimizers.domain.contract.MinimizersCalculatorFactoryContract
import hr.fer.minimizers.domain.contract.MinimizersRepositoryContract
import hr.fer.minimizers.domain.toData
import hr.fer.minimizers.domain.toHistoryData
import hr.fer.minimizertable.model.MinimizersTableData
import hr.fer.minimizertable.model.MinimizersTableHistoryData
import hr.fer.minimizertable.ui.history.contract.HistoryUseCaseContract
import hr.fer.minimizertable.ui.minimizers.contract.MinimizerTableUseCaseContract
import hr.fer.minimizertable.ui.result.contract.MinimizerTableResultUseCaseContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class MinimizersUseCase(
	private val minimizersRepository: MinimizersRepositoryContract,
	private val minimizersCalculatorFactory: MinimizersCalculatorFactoryContract
) : MinimizerTableUseCaseContract, MinimizerTableResultUseCaseContract, HistoryUseCaseContract {

	override fun getHistory(): Flow<ArrayList<MinimizersTableHistoryData>> {
		return minimizersRepository.getMinimizers()
			.map {
				it.map { item -> item.toHistoryData() } as ArrayList<MinimizersTableHistoryData>
			}
			.flowOn(Dispatchers.Main)
	}

	override fun deleteHistory(): Flow<Unit> {
		return minimizersRepository.deleteAll()
			.flowOn(Dispatchers.IO)
	}

	override fun calculateMinimizers(sequence: String, w: Int, k: Int): Flow<Boolean> {
		return flow {
			if (!sequence.contains("^[ACGT]*$".toRegex())) {
				throw Exception("Wrong data in field: Sequence")
			} else if (sequence.length < k) {
				throw Exception("Make sure k isn not larger than the sequence length")
			} else if (k > w) {
				throw Exception("Paramaeter k cant be larger than w")
			}

			val minimizers = minimizersCalculatorFactory.findMinimizersWithSteps(sequence, w, k)
			minimizersRepository.saveMinimizerData(minimizers)

			emit(true)
		}.flowOn(Dispatchers.IO)
	}

	override fun getMinimizersResult(sequence: String, w: Int, k: Int): Flow<MinimizersTableData> {
		val id = generateId(sequence, w, k)
		return minimizersRepository.getMinimizer(id)
			.flowOn(Dispatchers.IO)
			.map {
				it.toData()
			}
			.flowOn(Dispatchers.Main)
			.take(1)
	}

	private fun generateId(sequence: String, w: Int, k: Int): Int {
		return "$sequence$w$k".hashCode()
	}
}