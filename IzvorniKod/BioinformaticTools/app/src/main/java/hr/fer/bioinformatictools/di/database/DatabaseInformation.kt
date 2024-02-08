package hr.fer.bioinformatictools.di.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import hr.fer.dynamicprogramming.data.datasource.local.DynamicProgrammingDao
import hr.fer.dynamicprogramming.data.model.CellEntityModel
import hr.fer.dynamicprogramming.data.model.CellResultEntityModel
import hr.fer.dynamicprogramming.data.model.TableEntityModel
import hr.fer.fmindex.data.datasource.local.FMIndexDao
import hr.fer.fmindex.data.model.FMIndexEntityModel
import hr.fer.minimizers.data.datasource.local.MinimizersDao
import hr.fer.minimizers.data.model.MinimizerStepEntityModel
import hr.fer.minimizers.data.model.MinimizersEntityModel

/**
 * Contains all information about our realm database needed for it to function properly
 * @property databaseName represents the application database name
 * @property schemaVersion represents the current room schema version
 * @property migrate represents all migration functions needed to keep room up to date
 * @property GlobalSignInRoomDatabase Room database defining all room entities and DAOs that will be used with the database
 */
object DatabaseInformation {

	const val databaseName: String = "BioinformaticTools.room"
	const val schemaVersion: Int = 1

	@Database(
		entities = [
				TableEntityModel::class, CellEntityModel::class, CellResultEntityModel::class,
				MinimizersEntityModel::class, MinimizerStepEntityModel::class,
				FMIndexEntityModel::class
		   ],
		version = schemaVersion
	)
	abstract class BioinformaticToolsRoomDatabase : RoomDatabase() {

		/**
		 * Write all DAOs like this:
		 * abstract fun userDao(): UserDao
		 */

		abstract fun dynamicProgrammingDao(): DynamicProgrammingDao
		abstract fun minimizersDao(): MinimizersDao
		abstract fun fmIndexDao(): FMIndexDao
	}

	/**
	 * Example:
	 *
	 * DatabaseMigration.createDatabaseMigration(
	 * 		1, -> represents the old schema version (from what we are upgrading)
	 * 		2, -> represents the new schema version (to what we are upgrading)
	 * 		"UPDATE SET test1 = x FROM test",
	 * 		"UPDATE SET test2 = y FROM test2",
	 *	 	...
	 * 	)
	 *
	 * 	more here: https://developer.android.com/training/data-storage/room/migrating-db-versions
	 */
	val migrate = arrayListOf<Migration>(

	)
}