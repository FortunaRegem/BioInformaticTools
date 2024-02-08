package hr.fer.dynamicprogramming.data.repository

import hr.fer.dynamicprogramming.data.datasource.local.DynamicProgrammingDao
import hr.fer.dynamicprogramming.data.toEntityModel
import hr.fer.dynamicprogramming.data.toModel
import hr.fer.dynamicprogramming.domain.contract.TableRepositoryContract
import hr.fer.dynamicprogramming.domain.model.TableModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty

class TableRepository(
	private var dynamicProgrammingRoomDao: DynamicProgrammingDao
): TableRepositoryContract {

	override fun saveTableData(table: TableModel, type: Int): TableModel {
		val tableEntity = table.toEntityModel(type)
		dynamicProgrammingRoomDao.insert(tableEntity)

		table.tableData.map {
			val cellId = dynamicProgrammingRoomDao.insert(it.toEntityModel(tableEntity.tableId))
			dynamicProgrammingRoomDao.insert(it.valuePredecessors.map { item -> item.toEntityModel(cellId.toInt()) })
		}

		return table
	}

	override fun getTable(id: Int): Flow<TableModel> {
		return dynamicProgrammingRoomDao.getById(id)
			.map {
				it.toModel()
			}
			.onEmpty {
				throw Error("No table with given ID: $id")
			}
	}

	override fun getTables(type: Int): Flow<ArrayList<TableModel>> {
		return dynamicProgrammingRoomDao.getAll()
			.map {
				ArrayList(
					it
						.filter { item -> item.table?.type == type }
						.map { item -> item.toModel() }.sortedBy { item -> item.dateCreated }.reversed()
				)
			}
	}

	override fun deleteTables(type: Int): Flow<Unit> {
		return flow {
			dynamicProgrammingRoomDao.deleteByType(type)
			emit(Unit)
		}
	}
}