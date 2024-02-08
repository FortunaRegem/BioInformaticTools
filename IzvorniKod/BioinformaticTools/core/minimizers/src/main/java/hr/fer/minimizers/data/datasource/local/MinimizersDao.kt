package hr.fer.minimizers.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import hr.fer.common.constants.DatabaseConstants.MINIMIZERS_TABLE
import hr.fer.database.model.BaseDao
import hr.fer.minimizers.data.model.MinimizerStepEntityModel
import hr.fer.minimizers.data.model.MinimizerWithStepData
import hr.fer.minimizers.data.model.MinimizersEntityModel
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MinimizersDao : BaseDao<MinimizerWithStepData>(){

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(minimizers: MinimizersEntityModel): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(step: MinimizerStepEntityModel): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(steps: List<MinimizerStepEntityModel>): List<Long>

	@Delete
	abstract fun delete(minimizer: MinimizersEntityModel)

	@Transaction
	@Query("SELECT * FROM $MINIMIZERS_TABLE")
	abstract override fun getAll(): Flow<List<MinimizerWithStepData>>

	@Transaction
	@Query("SELECT * FROM $MINIMIZERS_TABLE WHERE minimizerId = :objectId")
	abstract override fun getById(objectId: Int): Flow<MinimizerWithStepData>

	@Query("DELETE FROM $MINIMIZERS_TABLE")
	abstract override fun deleteAll()

	@Query("SELECT COUNT(*) FROM $MINIMIZERS_TABLE")
	abstract override fun count(): Flow<Int>

}