package hr.fer.common.models.sequencealigner

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TableItem(
	var tableData: Array<CellItem>,
	var tableParameters: TableParametersItem,
	var minimumDistance: Int,
	var scorePosition: Pair<Int, Int>,
	var dateCreated: Long,
) {

	var onClick: ((TableItem) -> Unit)? = null

	fun onItemClick() {
		onClick?.invoke(this)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TableItem

		if (!tableData.contentDeepEquals(other.tableData)) return false
		if (tableParameters != other.tableParameters) return false
		if (minimumDistance != other.minimumDistance) return false
		if (dateCreated != other.dateCreated) return false

		return true
	}

	override fun hashCode(): Int {
		var result = tableData.contentDeepHashCode()
		result = 31 * result + tableParameters.hashCode()
		result = 31 * result + minimumDistance
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