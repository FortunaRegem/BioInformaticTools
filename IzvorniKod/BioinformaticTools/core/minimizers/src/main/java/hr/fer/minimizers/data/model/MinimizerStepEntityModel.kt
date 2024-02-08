package hr.fer.minimizers.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import hr.fer.common.constants.DatabaseConstants

@Entity(
	tableName = DatabaseConstants.MINIMIZERS_STEP_TABLE,
	foreignKeys = [ForeignKey(
		entity = MinimizersEntityModel::class,
		parentColumns = arrayOf("minimizerId"),
		childColumns = arrayOf("minimizerReferenceId"),
		onDelete = ForeignKey.CASCADE
	)],
	indices = [
		Index("minimizerReferenceId")
	]
)
data class MinimizerStepEntityModel(
	@PrimaryKey(autoGenerate = true) val minimizerStepId: Int,
	val minimizerReferenceId: Int,
	val currentStep: Int,
	val currentMinKmerStartPos: Int,
	val nextKmerStartPos: Int,
	val windowResultStartPos: Int,
	val hasNextKmer: Boolean,
	val currentWindowRow: Int,
	val currentWindowStartPos: Int,
	val currentWindow: String,
)