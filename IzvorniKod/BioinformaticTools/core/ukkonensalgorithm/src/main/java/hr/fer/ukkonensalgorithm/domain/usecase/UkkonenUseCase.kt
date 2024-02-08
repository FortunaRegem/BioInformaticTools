package hr.fer.ukkonensalgorithm.domain.usecase

import hr.fer.ukkonensalgorithm.domain.contract.UkkonenCalculatorFactoryContract
import hr.fer.ukkonensalgorithm.domain.toData
import hr.fer.ukkonenstree.model.SuffixTreeData
import hr.fer.ukkonenstree.ui.result.contract.UkkonensTreeResultUseCaseContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UkkonenUseCase(
//	private val fmIndexRepository: UkkonenRepositoryContract,
	private val fmIndexCalculatorFactory: UkkonenCalculatorFactoryContract
) : UkkonensTreeResultUseCaseContract {

//	override fun getHistory(): Flow<ArrayList<UkkonenTableHistoryData>> {
//		return fmIndexRepository.getUkkonen()
//			.map {
//				it.map { item -> item.toHistoryData() } as ArrayList<UkkonenTableHistoryData>
//			}
//			.flowOn(Dispatchers.Main)
//	}
//
//	override fun deleteHistory(): Flow<Unit> {
//		return fmIndexRepository.deleteAll()
//			.flowOn(Dispatchers.IO)
//	}

	override fun calculateUkkonen(sequence: String): Flow<ArrayList<SuffixTreeData>> {
		return flow {
//			if (!sequence.contains("^[ACGT]*$".toRegex())) {
//				throw Exception("Wrong data in field: Sequence")
//			}

			val listOfTrees = fmIndexCalculatorFactory.findUkkonenWithSteps(sequence)
//			fmIndexRepository.saveUkkonenData(fmIndex)

			emit(listOfTrees.map { it.toData() } as ArrayList<SuffixTreeData>)
		}.flowOn(Dispatchers.IO)
	}

//	override fun getUkkonenResult(sequence: String, pattern: String): Flow<UkkonenTableData> {
//		val id = generateId(sequence, pattern)
//		return fmIndexRepository.getUkkonen(id)
//			.flowOn(Dispatchers.IO)
//			.map {
//				it.toData()
//			}
//			.flowOn(Dispatchers.Main)
//			.take(1)
//	}
//
//	private fun generateId(sequence: String, pattern: String): Int {
//		return "${sequence.replace("$", "")}$pattern".hashCode()
//	}
}