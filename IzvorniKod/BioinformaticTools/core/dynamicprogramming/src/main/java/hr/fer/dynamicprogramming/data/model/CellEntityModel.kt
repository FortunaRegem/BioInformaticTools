package hr.fer.dynamicprogramming.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import hr.fer.common.constants.DatabaseConstants

@Entity(
	tableName = DatabaseConstants.DYNAMIC_PROGRAMMING_CELL,
	foreignKeys = [ForeignKey(
		entity = TableEntityModel::class,
		parentColumns = arrayOf("tableId"),
		childColumns = arrayOf("tableReferenceId"),
		onDelete = ForeignKey.CASCADE
	)],
	indices = [
		Index("tableReferenceId")
	]
)
data class CellEntityModel(
	@PrimaryKey(autoGenerate = true) val cellId: Int,
	val value: Int,
	@Embedded val position: IntPairDataModel?,
	val tableReferenceId: Int,
)

class CellWithPredecessorsData(
	@Embedded var row: CellEntityModel? = null,
	@Relation(
		parentColumn = "cellId",
		entityColumn = "cellReferenceId",
		entity = CellResultEntityModel::class
	)
	var valuePredecessors: List<CellResultEntityModel>? = null
)