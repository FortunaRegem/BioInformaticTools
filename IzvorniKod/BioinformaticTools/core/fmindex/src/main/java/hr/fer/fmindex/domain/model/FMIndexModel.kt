package hr.fer.fmindex.domain.model

class FMIndexModel(
	val sequence: String,
	val pattern: String,
	val suffixArray: List<Int>,
	val transformedStringBWT: String,
	val firstString: List<Pair<String, Int>>,
	val charRankings: Map<Char, List<Int>>,
	val resultsCount: Int,
	val dateCreated: Long,
)