package hr.fer.minimizertable.model

import hr.fer.common.models.BindableItem
import hr.fer.minimizertable.BR
import hr.fer.minimizertable.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MinimizersTableHistoryData(
	val sequence: String,
	val w: Int,
	val k: Int,
	val dateCreated: Long,
) {
	var onClick: ((MinimizersTableHistoryData) -> Unit)? = null

	fun onItemClick() {
		onClick?.invoke(this)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as MinimizersTableHistoryData

		if (sequence != other.sequence) return false
		if (w != other.w) return false
		if (k != other.k) return false
		if (dateCreated != other.dateCreated) return false

		return true
	}

	override fun hashCode(): Int {
		var result = sequence.hashCode()
		result = 31 * result + w
		result = 31 * result + k
		result = 31 * result + dateCreated.hashCode()
		return result
	}

	private var formatter: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault())

	fun getDate(): String {
		return formatter.format(
			Date().apply {
				this.time = dateCreated
			}
		)
	}
}

fun MinimizersTableHistoryData.toBindableItem() = BindableItem(
	data = this,
	variableId = BR.tableData,
	layoutId = R.layout.item_minimizer_history
)