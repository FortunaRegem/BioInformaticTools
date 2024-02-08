package hr.fer.ukkonensalgorithm.domain.contract

import hr.fer.ukkonensalgorithm.domain.model.SuffixTreeModel
import kotlinx.coroutines.flow.Flow

interface UkkonenRepositoryContract {
	fun saveUkkonenData(fmIndex: SuffixTreeModel): SuffixTreeModel
	fun getUkkonen(): Flow<ArrayList<SuffixTreeModel>>
	fun getUkkonen(id: Int): Flow<SuffixTreeModel>
	fun deleteAll(): Flow<Unit>
}