package hr.fer.dynamicprogramming.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import hr.fer.common.constants.DatabaseConstants

@Entity(
	tableName = DatabaseConstants.DYNAMIC_PROGRAMMING_CELL_RESULT,
	foreignKeys = [ForeignKey(
		entity = CellEntityModel::class,
		parentColumns = arrayOf("cellId"),
		childColumns = arrayOf("cellReferenceId"),
		onDelete = ForeignKey.CASCADE
	)],
	indices = [
		Index("cellReferenceId")
	]
)
data class CellResultEntityModel(
	@PrimaryKey(autoGenerate = true) val id: Int,
	@Embedded val predecessor: IntPairDataModel?,
	val seqA: String,
	val result: String,
	val seqB: String,
	val cellReferenceId: Int
)