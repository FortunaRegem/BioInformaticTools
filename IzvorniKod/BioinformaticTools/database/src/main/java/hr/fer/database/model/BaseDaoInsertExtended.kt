package hr.fer.database.model

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

abstract class BaseDaoInsertExtended<T> : BaseDao<T>() {

	/**
	 * This works only on simple Entities that for example dont have nested lists that need relations and such
	 **/
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(obj: T): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract fun insert(objList: List<T>): List<Long>

	@Update
	abstract fun update(obj: T)

	@Update
	abstract fun update(objList: List<T>)

	@Delete
	abstract fun delete(obj: T)

	@Delete
	abstract fun delete(objList: List<T>)

}