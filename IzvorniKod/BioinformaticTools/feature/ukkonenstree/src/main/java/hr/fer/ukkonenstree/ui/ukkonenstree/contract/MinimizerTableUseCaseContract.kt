package hr.fer.ukkonenstree.ui.ukkonenstree.contract

import kotlinx.coroutines.flow.Flow

interface MinimizerTableUseCaseContract {
	fun calculateMinimizers(sequence: String, w: Int, k: Int): Flow<Boolean>
}