package hr.fer.dynamicprogramming.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import hr.fer.common.constants.DatabaseConstants.DYNAMIC_PROGRAMMING_TABLE
import hr.fer.database.model.BaseDao
import hr.fer.dynamicprogramming.data.model.CellEntityModel
import hr.fer.dynamicprogramming.data.model.CellResultEntityModel
import hr.fer.dynamicprogramming.data.model.TableEntityModel
import hr.fer.dynamicprogramming.data.model.TableWithCellData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DynamicProgrammingDao : BaseDao<TableWithCellData>() {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(table: TableEntityModel): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(cells: CellEntityModel): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(cells: List<CellResultEntityModel>): List<Long>

	@Delete
	abstract fun delete(table: TableEntityModel)

	@Transaction
	@Query("SELECT * FROM $DYNAMIC_PROGRAMMING_TABLE")
	abstract override fun getAll(): Flow<List<TableWithCellData>>

	@Transaction
	@Query("SELECT * FROM $DYNAMIC_PROGRAMMING_TABLE WHERE tableId = :objectId")
	abstract override fun getById(objectId: Int): Flow<TableWithCellData>

	@Query("DELETE FROM $DYNAMIC_PROGRAMMING_TABLE")
	abstract override fun deleteAll()

	@Query("DELETE FROM $DYNAMIC_PROGRAMMING_TABLE WHERE type = :type")
	abstract fun deleteByType(type: Int)

	@Query("SELECT COUNT(*) FROM $DYNAMIC_PROGRAMMING_TABLE")
	abstract override fun count(): Flow<Int>

}