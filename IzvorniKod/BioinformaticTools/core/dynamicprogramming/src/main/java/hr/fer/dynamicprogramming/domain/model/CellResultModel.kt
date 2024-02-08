package hr.fer.dynamicprogramming.domain.model

data class CellResultModel(
	var predecessor: Pair<Int, Int>,
	var seqA: String,
	var result: String,
	var seqB: String
)