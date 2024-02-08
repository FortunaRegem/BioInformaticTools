package hr.fer.fmindex.data.repository

import hr.fer.fmindex.data.datasource.local.FMIndexDao
import hr.fer.fmindex.data.toEntityModel
import hr.fer.fmindex.data.toModel
import hr.fer.fmindex.domain.contract.FMIndexRepositoryContract
import hr.fer.fmindex.domain.model.FMIndexModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty

class FMIndexRepository(
	private val fmIndexDao: FMIndexDao
) : FMIndexRepositoryContract {

	override fun saveFMIndexData(fmIndex: FMIndexModel): FMIndexModel {
		val fmIndexEntity = fmIndex.toEntityModel()
		fmIndexDao.insert(fmIndexEntity)

//		fmIndex.steps.map {
//			fmIndexDao.insert(it.toEntityModel(fmIndexEntity.fmIndexId))
//		}

		return fmIndex
	}

	override fun getFMIndex(): Flow<ArrayList<FMIndexModel>> {
		return fmIndexDao.getAll()
			.map {
				ArrayList(
					it
						.map { item -> item.toModel() }.sortedBy { item -> item.dateCreated }.reversed()
				)
			}
	}

	override fun getFMIndex(id: Int): Flow<FMIndexModel> {
		return fmIndexDao.getById(id)
			.map {
				it.toModel()
			}
			.onEmpty {
				throw Error("No FMIndex with given ID: $id")
			}
	}

	override fun deleteAll(): Flow<Unit> {
		return flow {
			fmIndexDao.deleteAll()
			emit(Unit)
		}
	}

}