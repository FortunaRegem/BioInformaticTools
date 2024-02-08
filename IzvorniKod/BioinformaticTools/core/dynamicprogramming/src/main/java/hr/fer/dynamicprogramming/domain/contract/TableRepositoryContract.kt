package hr.fer.dynamicprogramming.domain.contract

import hr.fer.dynamicprogramming.domain.model.TableModel
import kotlinx.coroutines.flow.Flow

interface TableRepositoryContract {
	fun saveTableData(table: TableModel, type: Int): TableModel
	fun getTable(id: Int): Flow<TableModel>
	fun getTables(type: Int): Flow<ArrayList<TableModel>>
	fun deleteTables(type: Int): Flow<Unit>
}