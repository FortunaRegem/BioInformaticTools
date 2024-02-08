package hr.fer.minimizers.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import hr.fer.common.constants.DatabaseConstants

@Entity(tableName = DatabaseConstants.MINIMIZERS_TABLE)
data class MinimizersEntityModel(
	@PrimaryKey(autoGenerate = false) val minimizerId: Int,
	val sequence: String,
	val w: Int,
	val k: Int,
	val maxWindowsRow: Int,
	val dateCreated: Long,
)

class MinimizerWithStepData(
	@Embedded var minimizer: MinimizersEntityModel? = null,
	@Relation(
		parentColumn = "minimizerId",
		entityColumn = "minimizerReferenceId",
		entity = MinimizerStepEntityModel::class
	)
	var minimizerSteps: List<MinimizerStepEntityModel>? = null
)