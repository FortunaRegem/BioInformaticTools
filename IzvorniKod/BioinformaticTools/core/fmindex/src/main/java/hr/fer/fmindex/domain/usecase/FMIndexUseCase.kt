package hr.fer.fmindex.domain.usecase

import hr.fer.fmindex.domain.contract.FMIndexCalculatorFactoryContract
import hr.fer.fmindex.domain.contract.FMIndexRepositoryContract
import hr.fer.fmindex.domain.toData
import hr.fer.fmindex.domain.toHistoryData
import hr.fer.fmindextable.model.FMIndexTableData
import hr.fer.fmindextable.model.FMIndexTableHistoryData
import hr.fer.fmindextable.ui.fmindex.contract.FMIndexTableUseCaseContract
import hr.fer.fmindextable.ui.history.contract.HistoryUseCaseContract
import hr.fer.fmindextable.ui.result.contract.FmIndexTableResultUseCaseContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class FMIndexUseCase(
	private val fmIndexRepository: FMIndexRepositoryContract,
	private val fmIndexCalculatorFactory: FMIndexCalculatorFactoryContract
) : FMIndexTableUseCaseContract, FmIndexTableResultUseCaseContract, HistoryUseCaseContract {

	override fun getHistory(): Flow<ArrayList<FMIndexTableHistoryData>> {
		return fmIndexRepository.getFMIndex()
			.map {
				it.map { item -> item.toHistoryData() } as ArrayList<FMIndexTableHistoryData>
			}
			.flowOn(Dispatchers.Main)
	}

	override fun deleteHistory(): Flow<Unit> {
		return fmIndexRepository.deleteAll()
			.flowOn(Dispatchers.IO)
	}

	override fun calculateFMIndex(sequence: String, pattern: String): Flow<Boolean> {
		return flow {
			if (!sequence.contains("^[ACGT]*$".toRegex())) {
				throw Exception("Wrong data in field: Sequence")
			} else if (!pattern.contains("^[ACGT]*$".toRegex())) {
				throw Exception("Wrong data in field: Pattern")
			} else if (pattern.length > sequence.length) {
				throw Exception("Parameter pattern cant be larger than the sequence")
			}

			val fmIndex = fmIndexCalculatorFactory.findFMIndexWithSteps(sequence, pattern)
			fmIndexRepository.saveFMIndexData(fmIndex)

			emit(true)
		}.flowOn(Dispatchers.IO)
	}

	override fun getFMIndexResult(sequence: String, pattern: String): Flow<FMIndexTableData> {
		val id = generateId(sequence, pattern)
		return fmIndexRepository.getFMIndex(id)
			.flowOn(Dispatchers.IO)
			.map {
				it.toData()
			}
			.flowOn(Dispatchers.Main)
			.take(1)
	}

	private fun generateId(sequence: String, pattern: String): Int {
		return "${sequence.replace("$", "")}$pattern".hashCode()
	}
}