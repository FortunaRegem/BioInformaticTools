package hr.fer.common.models.sequencealigner


data class AlignmentResultItem(
	var position: Int = 0,
	var resultLineSequenceA: String = "",
	var resultLIneActionsTaken: String = "",
	var resultLineSequenceB: String = ""
) {

	var onClick: ((AlignmentResultItem) -> Unit)? = null

	fun onItemClick() {
		onClick?.invoke(this)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as AlignmentResultItem

		if (position != other.position) return false
		if (resultLineSequenceA != other.resultLineSequenceA) return false
		if (resultLIneActionsTaken != other.resultLIneActionsTaken) return false
		if (resultLineSequenceB != other.resultLineSequenceB) return false

		return true
	}

	override fun hashCode(): Int {
		var result = position
		result = 31 * result + resultLineSequenceA.hashCode()
		result = 31 * result + resultLIneActionsTaken.hashCode()
		result = 31 * result + resultLineSequenceB.hashCode()
		return result
	}


}



