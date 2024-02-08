package hr.fer.common.models.sequencealigner

class TableParametersItem(
	var sequenceA: String,
	var sequenceB: String,
	var rowCount: Int,
	var columnCount: Int,
	var match: Int,
	var mismatch: Int,
	var gap: Int,
	var distance: Boolean
)