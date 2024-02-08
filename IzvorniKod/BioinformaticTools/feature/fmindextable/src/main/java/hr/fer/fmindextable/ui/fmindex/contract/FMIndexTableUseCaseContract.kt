package hr.fer.fmindextable.ui.fmindex.contract

import kotlinx.coroutines.flow.Flow

interface FMIndexTableUseCaseContract {
	fun calculateFMIndex(sequence: String, pattern: String): Flow<Boolean>
}