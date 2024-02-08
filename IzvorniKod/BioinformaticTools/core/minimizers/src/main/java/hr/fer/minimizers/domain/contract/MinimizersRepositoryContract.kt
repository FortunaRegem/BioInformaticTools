package hr.fer.minimizers.domain.contract

import hr.fer.minimizers.domain.model.MinimizersModel
import kotlinx.coroutines.flow.Flow

interface MinimizersRepositoryContract {
	fun saveMinimizerData(minimizers: MinimizersModel): MinimizersModel
	fun getMinimizers(): Flow<ArrayList<MinimizersModel>>
	fun getMinimizer(id: Int): Flow<MinimizersModel>
	fun deleteAll(): Flow<Unit>
}