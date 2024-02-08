package hr.fer.localsequencealigner.ui.history.contract

import hr.fer.common.models.sequencealigner.TableItem
import kotlinx.coroutines.flow.Flow

interface HistoryLocalUseCaseContract {
	fun getHistoryLocal(): Flow<ArrayList<TableItem>>
	fun deleteHistoryLocal(): Flow<Unit>
}