package hr.fer.minimizertable.model

data class MinimizersTableData(
	val sequence: String,
	val w: Int,
	val k: Int,
	val maxWindowRow: Int,
	val steps: ArrayList<MinimizerStepData>
)