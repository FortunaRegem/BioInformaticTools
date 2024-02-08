package hr.fer.minimizertable.ui.minimizers.contract

import kotlinx.coroutines.flow.Flow

interface MinimizerTableUseCaseContract {
	fun calculateMinimizers(sequence: String, w: Int, k: Int): Flow<Boolean>
}