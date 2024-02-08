package hr.fer.minimizers.domain.model

data class MinimizerStepModel(
	val currentStep: Int,
	val currentMinKmerStartPos: Int,
	val nextKmerStartPos: Int,
	val windowResultStartPos: Int,
	val hasNextKmer: Boolean,
	val currentWindowRow: Int,
	val currentWindowStartPos: Int,
	val currentWindow: String,
)