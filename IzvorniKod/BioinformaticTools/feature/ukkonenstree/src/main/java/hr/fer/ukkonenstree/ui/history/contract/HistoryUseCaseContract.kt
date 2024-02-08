package hr.fer.ukkonenstree.ui.history.contract

import kotlinx.coroutines.flow.Flow

interface HistoryUseCaseContract {
//	fun getHistory(): Flow<ArrayList<MinimizersTableHistoryData>>
	fun deleteHistory(): Flow<Unit>
}