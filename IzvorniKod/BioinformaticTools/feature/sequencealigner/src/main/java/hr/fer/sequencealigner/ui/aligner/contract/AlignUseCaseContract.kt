package hr.fer.sequencealigner.ui.aligner.contract

import hr.fer.common.models.sequencealigner.AlignmentResultItem
import hr.fer.common.models.sequencealigner.TableItem
import kotlinx.coroutines.flow.Flow

interface AlignUseCaseContract {
	fun alignGlobal(sequenceA: String, sequenceB: String, match: Int, mismatch: Int, gap: Int, isDistance: Boolean): Flow<Boolean>
	fun getAlignmentResults(sequenceA: String, sequenceB: String): Flow<Pair<ArrayList<AlignmentResultItem>, Int>>
	fun getTable(sequenceA: String, sequenceB: String): Flow<TableItem>
}