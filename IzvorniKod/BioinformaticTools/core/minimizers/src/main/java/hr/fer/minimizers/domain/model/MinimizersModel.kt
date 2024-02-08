package hr.fer.minimizers.domain.model

class MinimizersModel(
	val sequence: String,
	val w: Int,
	val k: Int,
	val maxWindowsRow: Int,
	val steps: ArrayList<MinimizerStepModel>,
	val dateCreated: Long,
)
