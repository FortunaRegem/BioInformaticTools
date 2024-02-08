package hr.fer.dynamicprogramming.data.factory

import hr.fer.dynamicprogramming.domain.contract.MinimumDistanceTableFactoryContract
import hr.fer.dynamicprogramming.domain.model.CellModel
import hr.fer.dynamicprogramming.domain.model.CellResultModel
import hr.fer.dynamicprogramming.domain.model.TableModel
import hr.fer.dynamicprogramming.domain.model.TableParametersModel

class MinimumDistanceTableFactory : MinimumDistanceTableFactoryContract {

	override fun calculateGlobalDistanceTable(
		sequenceA: String,
		sequenceB: String,
		match: Int,
		mismatch: Int,
		gap: Int,
		isDistance: Boolean
	): TableModel {
		val rows = sequenceA.length
		val columns = sequenceB.length

		val table = Array((rows + 1) * (columns + 1)) { tableIndex ->
			val rowIndex = tableIndex / (columns + 1)
			val columnIndex = tableIndex % (columns + 1)
			val position = Pair(rowIndex, columnIndex)

			when {
				rowIndex == 0 -> {
					CellModel(
						columnIndex * gap,
						position,
						if (columnIndex != 0)
							arrayListOf(
								CellResultModel(
									predecessor = Pair(rowIndex, columnIndex - 1),
									seqA = "_",
									result = " ",
									seqB = sequenceB[columnIndex - 1].toString()
								)
							)
						else arrayListOf()
					)
				}
				columnIndex == 0 -> {
					CellModel(
						rowIndex * gap,
						position,
						arrayListOf(
							CellResultModel(
								predecessor = Pair(rowIndex - 1, columnIndex),
								seqA = sequenceA[rowIndex - 1].toString(),
								result = " ",
								seqB = "_"
							)
						)
					)
				}
				else -> CellModel(0, position, arrayListOf())
			}
		}

		for (i in 1..rows step 1) {
			for (j in 1..columns step 1) {
				val currentIndex = (columns + 1) * i + j
				val topLeftIndex = (columns + 1) * (i - 1) + (j - 1)
				val topIndex = (columns + 1) * (i - 1) + j
				val leftIndex = (columns + 1) * i + (j - 1)

				val cellValue: Int

				if (isDistance) {
					var min = if (sequenceA[i - 1] == sequenceB[j - 1]) {
						table[topLeftIndex].value + match
					} else {
						rows * columns
					}
					min = minOf(
						min,
						table[topLeftIndex].value + mismatch,
						table[topIndex].value + gap,
						table[leftIndex].value + gap
					)

					cellValue = min
				} else {
					var max = if (sequenceA[i - 1] == sequenceB[j - 1]) {
						table[topLeftIndex].value + match
					} else {
						-1 * rows * columns
					}
					max = maxOf(
						max,
						table[topLeftIndex].value + mismatch,
						table[topIndex].value + gap,
						table[leftIndex].value + gap
					)

					cellValue = max
				}

				table[currentIndex].value = cellValue
				if (table[topLeftIndex].value + match == cellValue && sequenceA[i - 1] == sequenceB[j - 1]) {
					table[currentIndex].valuePredecessors.add(
						CellResultModel(
							predecessor = table[topLeftIndex].position,
							seqA = sequenceA[i - 1].toString(),
							result = "*",
							seqB = sequenceB[j - 1].toString()
						)
					)
				}
				if (table[topLeftIndex].value + mismatch == cellValue && sequenceA[i - 1] != sequenceB[j - 1]) {
					table[currentIndex].valuePredecessors.add(
						CellResultModel(
							predecessor = table[topLeftIndex].position,
							seqA = sequenceA[i - 1].toString(),
							result = "|",
							seqB = sequenceB[j - 1].toString()
						)
					)
				}
				if (table[topIndex].value + gap == cellValue) {
					table[currentIndex].valuePredecessors.add(
						CellResultModel(
							predecessor = table[topIndex].position,
							seqA = sequenceA[i - 1].toString(),
							result = " ",
							seqB = "_"
						)
					)
				}
				if (table[leftIndex].value + gap == cellValue) {
					table[currentIndex].valuePredecessors.add(
						CellResultModel(
							predecessor = table[leftIndex].position,
							seqA = "_",
							result = " ",
							seqB = sequenceB[j - 1].toString()
						)
					)
				}
			}
		}
		return TableModel(
			table,
			TableParametersModel(
				sequenceA,
				sequenceB,
				rows,
				columns,
				match,
				mismatch,
				gap,
				isDistance
			),
			table.last().value,
			table.last().position,
			dateCreated = System.currentTimeMillis()
		)
	}

	override fun calculateLocalDistanceTable(
		sequenceA: String,
		sequenceB: String,
		match: Int,
		mismatch: Int,
		gap: Int
	): TableModel {
		val rows = sequenceA.length
		val columns = sequenceB.length

		val table = Array((rows + 1) * (columns + 1)) { tableIndex ->
			val rowIndex = tableIndex / (columns + 1)
			val columnIndex = tableIndex % (columns + 1)
			val position = Pair(rowIndex, columnIndex)

			CellModel(
				0,
				position,
				arrayListOf()
			)
		}

		for (i in 1..rows) {
			for (j in 1..columns) {
				val currentIndex = (columns + 1) * i + j
				val topLeftIndex = (columns + 1) * (i - 1) + (j - 1)
				val topIndex = (columns + 1) * (i - 1) + j
				val leftIndex = (columns + 1) * i + (j - 1)

				val scoreDiagonal = if (sequenceA[i - 1] == sequenceB[j - 1]) {
					table[topLeftIndex].value + match
				} else {
					table[topLeftIndex].value + mismatch
				}
				val scoreUp = table[topIndex].value + gap
				val scoreLeft = table[leftIndex].value + gap

				val cellScore = maxOf(0, scoreDiagonal, scoreUp, scoreLeft)
				table[currentIndex].value = cellScore

				if (cellScore == 0) continue

				if (cellScore == scoreDiagonal) {
					table[currentIndex].valuePredecessors.add(
						CellResultModel(
							predecessor = table[topLeftIndex].position,
							seqA = if (sequenceA[i - 1] == sequenceB[j - 1]) sequenceA[i - 1].toString() else "_",
							result = if (sequenceA[i - 1] == sequenceB[j - 1]) "*" else "|",
							seqB = if (sequenceA[i - 1] == sequenceB[j - 1]) sequenceB[j - 1].toString() else "_"
						)
					)
				}
				if (cellScore == scoreLeft) {
					table[currentIndex].valuePredecessors.add(
						CellResultModel(
							predecessor = table[leftIndex].position,
							seqA = "_",
							result = " ",
							seqB = sequenceB[j - 1].toString()
						)
					)
				}
				if (cellScore == scoreUp) {
					table[currentIndex].valuePredecessors.add(
						CellResultModel(
							predecessor = table[topIndex].position,
							seqA = sequenceA[i - 1].toString(),
							result = " ",
							seqB = "_"
						)
					)
				}
			}
		}

		var maxScore = 0
		var maxPosition = Pair(0, 0)
		table.forEach {
			if (it.value > maxScore) {
				maxScore = it.value
				maxPosition = it.position
			}
		}

		return TableModel(
			table,
			TableParametersModel(
				sequenceA,
				sequenceB,
				rows,
				columns,
				match,
				mismatch,
				gap,
				false
			),
			maxScore,
			maxPosition,
			dateCreated = System.currentTimeMillis()
		)
	}

}