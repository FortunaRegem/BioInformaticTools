package hr.fer.fmindextable.model

import hr.fer.common.models.BindableItem
import hr.fer.fmindextable.BR
import hr.fer.fmindextable.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FMIndexTableHistoryData(
	val sequence: String,
	val pattern: String,
	val dateCreated: Long,
) {
	var onClick: ((FMIndexTableHistoryData) -> Unit)? = null

	fun onItemClick() {
		onClick?.invoke(this)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as FMIndexTableHistoryData

		if (sequence != other.sequence) return false
		if (pattern != other.pattern) return false
		if (dateCreated != other.dateCreated) return false

		return true
	}

	override fun hashCode(): Int {
		var result = sequence.hashCode()
		result = 31 * result + pattern.hashCode()
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

fun FMIndexTableHistoryData.toBindableItem() = BindableItem(
	data = this,
	variableId = BR.tableData,
	layoutId = R.layout.item_fmindex_history
)