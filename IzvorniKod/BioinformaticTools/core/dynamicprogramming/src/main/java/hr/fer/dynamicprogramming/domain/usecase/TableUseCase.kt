package hr.fer.dynamicprogramming.domain.usecase

import hr.fer.common.models.sequencealigner.AlignmentResultItem
import hr.fer.common.models.sequencealigner.TableItem
import hr.fer.dynamicprogramming.domain.contract.MinimumDistanceTableFactoryContract
import hr.fer.dynamicprogramming.domain.contract.TableRepositoryContract
import hr.fer.dynamicprogramming.domain.model.TableModel
import hr.fer.dynamicprogramming.domain.model.TableTypeEnum
import hr.fer.dynamicprogramming.domain.toItem
import hr.fer.localsequencealigner.ui.aligner.contract.AlignLocalUseCaseContract
import hr.fer.localsequencealigner.ui.history.contract.HistoryLocalUseCaseContract
import hr.fer.sequencealigner.ui.aligner.contract.AlignUseCaseContract
import hr.fer.sequencealigner.ui.history.contract.HistoryUseCaseContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class TableUseCase(
	private var tableRepository: TableRepositoryContract,
	private var minimumDistanceTableFactory: MinimumDistanceTableFactoryContract
) : AlignUseCaseContract, HistoryUseCaseContract, AlignLocalUseCaseContract, HistoryLocalUseCaseContract {

	override fun alignGlobal(sequenceA: String, sequenceB: String, match: Int, mismatch: Int, gap: Int, isDistance: Boolean): Flow<Boolean> {
		return align(
			sequenceA,
			sequenceB,
			match,
			mismatch,
			gap,
			TableTypeEnum.GLOBAL_ALIGNMENT_TABLE
		) {
			minimumDistanceTableFactory.calculateGlobalDistanceTable(
				sequenceA,
				sequenceB,
				match,
				mismatch,
				gap,
				isDistance
			)
		}
	}

	override fun alignLocal(
		sequenceA: String,
		sequenceB: String,
		match: Int,
		mismatch: Int,
		gap: Int
	): Flow<Boolean> {
		return align(
			sequenceA,
			sequenceB,
			match,
			mismatch,
			gap,
			TableTypeEnum.LOCAL_ALIGNMENT_TABLE
		) {
			minimumDistanceTableFactory.calculateLocalDistanceTable(
				sequenceA,
				sequenceB,
				match,
				mismatch,
				gap
			)
		}
	}

	private inline fun align(sequenceA: String, sequenceB: String, match: Int, mismatch: Int, gap: Int, type: TableTypeEnum, crossinline function: (() -> TableModel)): Flow<Boolean> {
		return flow {
			if (!sequenceA.contains("^[ACGT]*$".toRegex())) {
				throw Exception("Wrong data in field: Sequence a")
			} else if (!sequenceB.contains("^[ACGT]*$".toRegex())) {
				throw Exception("Wrong data in field: Sequence b")
			} else if (match == 0 && mismatch == 0 && gap == 0) {
				throw Exception("Please enter at least one scoring value")
			}
			val table = function.invoke()
			tableRepository.saveTableData(table, type.id)

			emit(true)
		}.flowOn(Dispatchers.IO)
	}

	override fun getHistoryGlobal(): Flow<ArrayList<TableItem>> {
		return getHistory(TableTypeEnum.GLOBAL_ALIGNMENT_TABLE)
	}

	override fun getHistoryLocal(): Flow<ArrayList<TableItem>> {
		return getHistory(TableTypeEnum.LOCAL_ALIGNMENT_TABLE)
	}

	private fun getHistory(type: TableTypeEnum): Flow<ArrayList<TableItem>> {
		return tableRepository.getTables(type.id)
			.map {
				it.map { item -> item.toItem() } as ArrayList<TableItem>
			}
			.flowOn(Dispatchers.Main)
	}

	override fun deleteHistoryGlobal(): Flow<Unit> {
		return deleteHistory(TableTypeEnum.GLOBAL_ALIGNMENT_TABLE)
	}

	override fun deleteHistoryLocal(): Flow<Unit> {
		return deleteHistory(TableTypeEnum.LOCAL_ALIGNMENT_TABLE)
	}

	private fun deleteHistory(type: TableTypeEnum): Flow<Unit> {
		return tableRepository.deleteTables(type.id)
			.flowOn(Dispatchers.IO)
	}

	override fun getAlignmentResults(sequenceA: String, sequenceB: String): Flow<Pair<ArrayList<AlignmentResultItem>, Int>> {
		val id = generateId(sequenceA, sequenceB, TableTypeEnum.GLOBAL_ALIGNMENT_TABLE.id)
		return tableRepository.getTable(id)
			.flowOn(Dispatchers.IO)
			.map { tableModel ->
				findResults(tableModel)
			}
			.flowOn(Dispatchers.Main)
			.take(1)
	}

	override fun getAlignmentResultsLocal(sequenceA: String, sequenceB: String): Flow<Pair<ArrayList<AlignmentResultItem>, Int>> {
		val id = generateId(sequenceA, sequenceB, TableTypeEnum.LOCAL_ALIGNMENT_TABLE.id)
		return tableRepository.getTable(id)
			.flowOn(Dispatchers.IO)
			.map { tableModel ->
				findResults(tableModel)
			}
			.flowOn(Dispatchers.Main)
			.take(1)
	}

	override fun getTable(sequenceA: String, sequenceB: String): Flow<TableItem> {
		return tableRepository.getTable(generateId(sequenceA, sequenceB, TableTypeEnum.GLOBAL_ALIGNMENT_TABLE.id))
			.map {
				it.toItem()
			}
			.flowOn(Dispatchers.Main)

	}

	override fun getTableLocal(sequenceA: String, sequenceB: String): Flow<TableItem> {
		return tableRepository.getTable(generateId(sequenceA, sequenceB, TableTypeEnum.LOCAL_ALIGNMENT_TABLE.id))
			.map {
				it.toItem()
			}
			.flowOn(Dispatchers.Main)

	}

	private fun generateId(sequenceA: String, sequenceB: String, type: Int): Int {
		return "$sequenceA-$sequenceB-$type".hashCode()
	}

	private fun findResults(tableModel: TableModel): Pair<ArrayList<AlignmentResultItem>, Int> {
		return Pair(
			calculateResultsForTable(tableModel, Pair(tableModel.scorePosition.first, tableModel.scorePosition.second))
				.first
				.mapIndexed { index, item ->
					val res = item.split("-")
					AlignmentResultItem(
						index + 1,
						res[0],
						res[1],
						res[2]
					)
				} as ArrayList<AlignmentResultItem>,
			tableModel.minimumDistance
		)

	}

	private fun calculateResultsForTable(tableModel: TableModel, currentPosition: Pair<Int, Int>, resultCount: Int = 0): Pair<ArrayList<String>, Int> {
		val currentIndex = currentPosition.first * (tableModel.tableParameters.columnCount + 1) + currentPosition.second
		println(currentIndex)

		val result = arrayListOf<String>()
		var currentCount = resultCount

		if (tableModel.tableData[currentIndex].valuePredecessors.isEmpty()) {
			result.add(" - - ")
		}

		for (predecessor in tableModel.tableData[currentIndex].valuePredecessors) {
			if (currentCount >= 10) break

			if (predecessor.predecessor == Pair(0, 0)) {
				result.add("${predecessor.seqA}-${predecessor.result}-${predecessor.seqB}")
				currentCount++
			} else {
				val (recursionResult, newCount) = calculateResultsForTable(tableModel, predecessor.predecessor, currentCount)
				currentCount = newCount

				if (recursionResult.isNotEmpty()) {
					recursionResult.forEach { resultString ->
						val strings = resultString.split("-")
						result.add("${strings[0] + predecessor.seqA}-${strings[1] + predecessor.result}-${strings[2] + predecessor.seqB}")
					}
				}
			}
		}

		return Pair(result, currentCount)
	}

}