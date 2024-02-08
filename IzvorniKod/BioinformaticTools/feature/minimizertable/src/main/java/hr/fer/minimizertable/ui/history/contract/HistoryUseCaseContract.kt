package hr.fer.minimizertable.ui.history.contract

import hr.fer.minimizertable.model.MinimizersTableHistoryData
import kotlinx.coroutines.flow.Flow

interface HistoryUseCaseContract {
	fun getHistory(): Flow<ArrayList<MinimizersTableHistoryData>>
	fun deleteHistory(): Flow<Unit>
}