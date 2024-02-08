package hr.fer.fmindex.domain.contract

import hr.fer.fmindex.domain.model.FMIndexModel
import kotlinx.coroutines.flow.Flow

interface FMIndexRepositoryContract {
	fun saveFMIndexData(fmIndex: FMIndexModel): FMIndexModel
	fun getFMIndex(): Flow<ArrayList<FMIndexModel>>
	fun getFMIndex(id: Int): Flow<FMIndexModel>
	fun deleteAll(): Flow<Unit>
}