package hr.fer.common.models.sequencealigner

class CellItem(
	var value: String,
	var position: Pair<Int, Int>,
	var valuePredecessors: ArrayList<CellResultItem>,
)