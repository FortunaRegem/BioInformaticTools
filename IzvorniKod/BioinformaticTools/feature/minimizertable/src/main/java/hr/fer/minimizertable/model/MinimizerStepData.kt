package hr.fer.minimizertable.model

class MinimizerStepData(
	val currentStep: Int,
	val currentMinKmerStartPos: Int,
	val nextKmerStartPos: Int,
	val windowResultStartPos: Int,
	val hasNextKmer: Boolean,
	val currentWindowRow: Int,
	val currentWindowStartPos: Int,
	val currentWindow: String,
)
