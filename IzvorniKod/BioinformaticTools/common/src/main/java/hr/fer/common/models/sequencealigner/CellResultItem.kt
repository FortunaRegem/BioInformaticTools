package hr.fer.common.models.sequencealigner

data class CellResultItem(
	var predecessor: Pair<Int, Int>,
	var seqA: String,
	var result: String,
	var seqB: String
)