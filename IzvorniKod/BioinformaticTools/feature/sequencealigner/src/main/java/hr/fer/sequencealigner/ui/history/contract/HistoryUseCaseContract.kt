package hr.fer.sequencealigner.ui.history.contract

import hr.fer.common.models.sequencealigner.TableItem
import kotlinx.coroutines.flow.Flow

interface HistoryUseCaseContract {
	fun getHistoryGlobal(): Flow<ArrayList<TableItem>>
	fun deleteHistoryGlobal(): Flow<Unit>
}