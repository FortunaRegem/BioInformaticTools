package hr.fer.bioinformatictools.di

import android.content.Context
import hr.fer.bioinformatictools.BuildConfig
import hr.fer.bioinformatictools.di.modules.DatabaseModule
import hr.fer.bioinformatictools.di.modules.ViewModelModule
import hr.fer.dynamicprogramming.di.DynamicProgrammingModule
import hr.fer.fmindex.di.FMIndexModule
import hr.fer.fmindextable.di.FMIndexTableModule
import hr.fer.localsequencealigner.di.LocalSequenceAlignerModule
import hr.fer.minimizers.di.MinimizersModule
import hr.fer.minimizertable.di.MinimizerTableModule
import hr.fer.sequencealigner.di.GlobalSequenceAlignerModule
import hr.fer.ukkonensalgorithm.di.UkkonenAlgorithmModule
import hr.fer.ukkonenstree.di.UkkonensTreeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

object BioinformaticToolsDI {
	fun startInjection(context: Context) {
		startKoin {
			androidLogger(Level.DEBUG)
			androidContext(context)
			modules(
				DatabaseModule.getDatabaseModule(),
				ViewModelModule.getViewModelModule(),

				//Navigation routes to all feature modules
				GlobalSequenceAlignerModule.getSequenceAlignerRoute(),
				LocalSequenceAlignerModule.getSequenceAlignerRoute(),
				MinimizerTableModule.getMinimizerTableRoute(),
				FMIndexTableModule.getFMIndexTableRoute(),
				UkkonensTreeModule.getUkkonensTreeRoute()
			)
		}


		//Add all core modules here
		DynamicProgrammingModule.inject()
		MinimizersModule.inject()
		FMIndexModule.inject()
		UkkonenAlgorithmModule.inject()

		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}
	}
}