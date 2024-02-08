package hr.fer.fmindextable.ui.result.contract

import hr.fer.fmindextable.model.FMIndexTableData
import kotlinx.coroutines.flow.Flow

interface FmIndexTableResultUseCaseContract {
	fun getFMIndexResult(sequence: String, pattern: String): Flow<FMIndexTableData>
}
