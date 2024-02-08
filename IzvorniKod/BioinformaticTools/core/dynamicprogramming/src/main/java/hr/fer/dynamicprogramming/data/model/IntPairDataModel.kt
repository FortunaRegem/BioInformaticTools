package hr.fer.dynamicprogramming.data.model

data class IntPairDataModel(
	val first: Int,
	val second: Int
) {

	constructor(pair: Pair<Int, Int>): this(pair.first, pair.second)

	fun getPair(): Pair<Int, Int> {
		return Pair(first, second)
	}
}