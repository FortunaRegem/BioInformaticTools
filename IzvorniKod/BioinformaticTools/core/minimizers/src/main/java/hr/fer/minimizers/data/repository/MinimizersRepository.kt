package hr.fer.minimizers.data.repository

import hr.fer.minimizers.data.datasource.local.MinimizersDao
import hr.fer.minimizers.data.toEntityModel
import hr.fer.minimizers.data.toModel
import hr.fer.minimizers.domain.contract.MinimizersRepositoryContract
import hr.fer.minimizers.domain.model.MinimizersModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty

class MinimizersRepository(
	private val minimizersDao: MinimizersDao
) : MinimizersRepositoryContract {

	override fun saveMinimizerData(minimizers: MinimizersModel): MinimizersModel {
		val minimizerEntity = minimizers.toEntityModel()
		minimizersDao.insert(minimizerEntity)

		minimizers.steps.map {
			minimizersDao.insert(it.toEntityModel(minimizerEntity.minimizerId))
		}

		return minimizers
	}

	override fun getMinimizers(): Flow<ArrayList<MinimizersModel>> {
		return minimizersDao.getAll()
			.map {
				ArrayList(
					it
						.map { item -> item.toModel() }.sortedBy { item -> item.dateCreated }.reversed()
				)
			}
	}

	override fun getMinimizer(id: Int): Flow<MinimizersModel> {
		return minimizersDao.getById(id)
			.map {
				it.toModel()
			}
			.onEmpty {
				throw Error("No minimizer with given ID: $id")
			}
	}

	override fun deleteAll(): Flow<Unit> {
		return flow {
			minimizersDao.deleteAll()
			emit(Unit)
		}
	}

}