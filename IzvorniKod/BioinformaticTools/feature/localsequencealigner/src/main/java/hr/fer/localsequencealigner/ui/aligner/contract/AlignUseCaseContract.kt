package hr.fer.localsequencealigner.ui.aligner.contract

import hr.fer.common.models.sequencealigner.AlignmentResultItem
import hr.fer.common.models.sequencealigner.TableItem
import kotlinx.coroutines.flow.Flow

interface AlignLocalUseCaseContract {
	fun alignLocal(sequenceA: String, sequenceB: String, match: Int, mismatch: Int, gap: Int): Flow<Boolean>
	fun getAlignmentResultsLocal(sequenceA: String, sequenceB: String): Flow<Pair<ArrayList<AlignmentResultItem>, Int>>
	fun getTableLocal(sequenceA: String, sequenceB: String): Flow<TableItem>
}