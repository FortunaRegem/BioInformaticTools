package hr.fer.ukkonenstree.ui.result.contract

import hr.fer.ukkonenstree.model.SuffixTreeData
import kotlinx.coroutines.flow.Flow

interface UkkonensTreeResultUseCaseContract {
	fun calculateUkkonen(sequence: String): Flow<ArrayList<SuffixTreeData>>
}
