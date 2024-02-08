package hr.fer.dynamicprogramming.domain.model

data class TableParametersModel(
	var sequenceA: String,
	var sequenceB: String,
	var rowCount: Int,
	var columnCount: Int,
	var match: Int,
	var mismatch: Int,
	var gap: Int,
	var distance: Boolean
)