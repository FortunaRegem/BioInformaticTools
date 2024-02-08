package hr.fer.database.setup

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import hr.fer.database.BuildConfig

class RoomDatabaseSetup<T : RoomDatabase>(
	private val dbClass: Class<T>,
	private val dbName: String,
	private val migrations: List<Migration>
) {

	fun createDatabase(context: Context): T {
		val builder = Room.databaseBuilder(context, dbClass, dbName)
			.addMigrations(*migrations.toTypedArray())

		if (!BuildConfig.DEBUG) {
			builder
				.fallbackToDestructiveMigration()  //Deletes database if migrations don't exist
		}
		return builder.build()
	}

}