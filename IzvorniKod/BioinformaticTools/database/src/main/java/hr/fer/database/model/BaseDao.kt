package hr.fer.database.model

import kotlinx.coroutines.flow.Flow

abstract class BaseDao<T> {

	/**
	 * These functions HAVE to be overridden if they want to be used, as they require a @Query annotation before they can work
	 */

	abstract fun getAll(): Flow<List<T>>

	abstract fun getById(objectId: Int): Flow<T>

	abstract fun deleteAll()

	abstract fun count(): Flow<Int>
}