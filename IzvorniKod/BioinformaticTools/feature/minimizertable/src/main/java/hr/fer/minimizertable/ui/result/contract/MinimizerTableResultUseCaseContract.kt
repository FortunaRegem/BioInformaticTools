package hr.fer.minimizertable.ui.result.contract

import hr.fer.minimizertable.model.MinimizersTableData
import kotlinx.coroutines.flow.Flow

interface MinimizerTableResultUseCaseContract {
	fun getMinimizersResult(sequence: String, w: Int, k: Int): Flow<MinimizersTableData>
}
