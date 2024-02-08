package hr.fer.fmindex.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import hr.fer.common.constants.DatabaseConstants

//@Entity(
//	tableName = DatabaseConstants.FM_INDEX_STEP_TABLE,
//	foreignKeys = [ForeignKey(
//		entity = FMIndexEntityModel::class,
//		parentColumns = arrayOf("fmIndexId"),
//		childColumns = arrayOf("fmIndexReferenceId"),
//		onDelete = ForeignKey.CASCADE
//	)],
//	indices = [
//		Index("minimizerReferenceId")
//	]
//)
//data class FMIndexStepEntityModel(
//	@PrimaryKey(autoGenerate = true) val fmIndexStepId: Int,
//	val fmIndexReferenceId: Int,
//	val currentPatternPosition: Int,
//)