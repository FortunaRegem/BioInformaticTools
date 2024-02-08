package hr.fer.dynamicprogramming.domain.model

data class CellModel(
	var value: Int,
	var position: Pair<Int, Int>,
	var valuePredecessors: ArrayList<CellResultModel>,
)