package hr.fer.database.setup

import androidx.room.migration.Migration

object DatabaseMigration {

	fun createDatabaseMigration(
		oldSchemaVersion: Int,
		newSchemaVersion: Int,
		vararg sqlQueries: String
	) =
		Migration(oldSchemaVersion, newSchemaVersion) {
			for (query in sqlQueries)
				it.execSQL(query)
		}
}