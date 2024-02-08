package hr.fer.dynamicprogramming.domain.model

data class TableModel(
	var tableData: Array<CellModel>,
	var tableParameters: TableParametersModel,
	var minimumDistance: Int,
	var scorePosition: Pair<Int, Int>,
	var dateCreated: Long
) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TableModel

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

}

enum class TableTypeEnum(val id: Int) {
	GLOBAL_ALIGNMENT_TABLE(1),
	LOCAL_ALIGNMENT_TABLE(2);

	companion object {

		fun fromId(id: Int) = TableTypeEnum.values().firstOrNull { it.id == id }
	}
}