package hr.fer.dynamicprogramming.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import hr.fer.common.constants.DatabaseConstants


@Entity(tableName = DatabaseConstants.DYNAMIC_PROGRAMMING_TABLE)
data class TableEntityModel(
	@PrimaryKey(autoGenerate = false) val tableId: Int,
	@Embedded val tableParameters: TableParametersDataModel?,
	val minimumDistance: Int,
	@Embedded val scorePosition: IntPairDataModel,
	val dateCreated: Long,
	val type: Int
)

class TableWithCellData(
	@Embedded var table: TableEntityModel? = null,
	@Relation(
		parentColumn = "tableId",
		entityColumn = "tableReferenceId",
		entity = CellEntityModel::class
	)
	var tableData: List<CellWithPredecessorsData>? = null
)