package hr.fer.dynamicprogramming.domain.contract

import hr.fer.dynamicprogramming.domain.model.TableModel

interface MinimumDistanceTableFactoryContract {
	fun calculateGlobalDistanceTable(sequenceA: String, sequenceB: String, match: Int, mismatch: Int, gap: Int, isDistance: Boolean): TableModel
	fun calculateLocalDistanceTable(sequenceA: String, sequenceB: String, match: Int, mismatch: Int, gap: Int): TableModel
}