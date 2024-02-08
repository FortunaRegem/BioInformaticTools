package hr.fer.fmindextable.ui.history.contract

import hr.fer.fmindextable.model.FMIndexTableHistoryData
import kotlinx.coroutines.flow.Flow

interface HistoryUseCaseContract {
	fun getHistory(): Flow<ArrayList<FMIndexTableHistoryData>>
	fun deleteHistory(): Flow<Unit>
}