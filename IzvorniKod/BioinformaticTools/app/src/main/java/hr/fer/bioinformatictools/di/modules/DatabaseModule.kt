package hr.fer.bioinformatictools.di.modules

import hr.fer.bioinformatictools.di.database.DatabaseInformation
import hr.fer.database.setup.RoomDatabaseSetup
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DatabaseModule {

	fun getDatabaseModule() = dataSourceModule

	private val dataSourceModule: Module = module {
		single {
			RoomDatabaseSetup(
				DatabaseInformation.BioinformaticToolsRoomDatabase::class.java,
				"bioinformatic_tools_database",
				listOf(/* migrations here */)
			).createDatabase(androidContext())
		}

		factory { get<DatabaseInformation.BioinformaticToolsRoomDatabase>().dynamicProgrammingDao() }
		factory { get<DatabaseInformation.BioinformaticToolsRoomDatabase>().minimizersDao() }
		factory { get<DatabaseInformation.BioinformaticToolsRoomDatabase>().fmIndexDao() }
	}
}