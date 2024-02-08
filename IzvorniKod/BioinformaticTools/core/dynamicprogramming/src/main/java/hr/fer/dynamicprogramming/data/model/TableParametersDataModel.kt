package hr.fer.dynamicprogramming.data.model

data class TableParametersDataModel(
	val sequenceA: String,
	val sequenceB: String,
	val rowCount: Int,
	val columnCount: Int,
	val match: Int,
	val mismatch: Int,
	val gap: Int,
	val distance: Boolean
)