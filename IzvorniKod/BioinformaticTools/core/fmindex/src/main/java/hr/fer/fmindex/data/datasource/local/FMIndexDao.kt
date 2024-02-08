package hr.fer.fmindex.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import hr.fer.common.constants.DatabaseConstants.FM_INDEX_TABLE
import hr.fer.database.model.BaseDao
import hr.fer.fmindex.data.model.FMIndexEntityModel
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FMIndexDao : BaseDao<FMIndexEntityModel>(){

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(fmIndex: FMIndexEntityModel): Long

//	@Insert(onConflict = OnConflictStrategy.REPLACE)
//	abstract fun insert(step: FMIndexStepEntityModel): Long

//	@Insert(onConflict = OnConflictStrategy.REPLACE)
//	abstract fun insert(steps: List<FMIndexStepEntityModel>): List<Long>

	@Delete
	abstract fun delete(fmIndex: FMIndexEntityModel)

	@Transaction
	@Query("SELECT * FROM $FM_INDEX_TABLE")
	abstract override fun getAll(): Flow<List<FMIndexEntityModel>>

	@Transaction
	@Query("SELECT * FROM $FM_INDEX_TABLE WHERE fmIndexId = :objectId")
	abstract override fun getById(objectId: Int): Flow<FMIndexEntityModel>

	@Query("DELETE FROM $FM_INDEX_TABLE")
	abstract override fun deleteAll()

	@Query("SELECT COUNT(*) FROM $FM_INDEX_TABLE")
	abstract override fun count(): Flow<Int>

}