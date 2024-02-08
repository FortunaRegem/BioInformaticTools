package hr.fer.fmindex.data.factory

import hr.fer.fmindex.domain.contract.FMIndexCalculatorFactoryContract
import hr.fer.fmindex.domain.model.FMIndexModel

class FMIndexCalculatorFactory : FMIndexCalculatorFactoryContract {

	override fun findFMIndexWithSteps(sequence: String, pattern: String): FMIndexModel {
		val sequenceWithEnd = "$sequence$"
		val bwtResult = generateBurrowsWheelerTransform(sequenceWithEnd)
		val suffixArray = generateSuffixArray(sequenceWithEnd)
		val (rankings, totalOccurrences) = calculateCharacterRankings(bwtResult)
		val firstColumn = calculateSortedFirstColumn(totalOccurrences)
		val firstString = calculateFirstString(totalOccurrences)
		val matches = findPatternMatches(firstColumn, rankings, pattern)

		return FMIndexModel(
			sequence = sequenceWithEnd,
			pattern = pattern,
			suffixArray = suffixArray,
			transformedStringBWT = bwtResult,
			firstString = firstString,
			charRankings = rankings,
			resultsCount = matches,
			dateCreated = System.currentTimeMillis(),
		)
	}

	private fun generateSuffixArray(input: String): List<Int> {
		return input.indices.map { it }
			.sortedBy { input.substring(it) }
	}

	private fun calculateFirstString(totalOccurrences: Map<Char, Int>): List<Pair<String, Int>> {
		val result = arrayListOf<Pair<String, Int>>()
		for (char in totalOccurrences.keys.sorted()) {
			for (i in 0 until (totalOccurrences[char] ?: 0)) {
				result.add(Pair(char.toString(), i + 1))
			}
		}

		return result
	}

	private fun generateBurrowsWheelerTransform(input: String): String {
		val rotations = input.indices.map { i -> input.substring(i) + input.substring(0, i) }
		val sortedRotations = rotations.sorted()
		return sortedRotations.joinToString("") { it.takeLast(1) }
	}

	private fun calculateCharacterRankings(bwt: String): Pair<Map<Char, List<Int>>, Map<Char, Int>> {
		val totalOccurrences = mutableMapOf<Char, Int>()
		val rankings = mutableMapOf<Char, MutableList<Int>>()

		bwt.toSet().forEach { char ->
			totalOccurrences[char] = 0
			rankings[char] = mutableListOf()
		}

		bwt.forEach { char ->
			totalOccurrences[char] = totalOccurrences[char]!! + 1

			totalOccurrences.keys.forEach { key ->
				rankings[key]!!.add(totalOccurrences[key]!!)
			}
		}

		return Pair(rankings, totalOccurrences)
	}

	private fun calculateSortedFirstColumn(totalOccurrences: Map<Char, Int>): Map<Char, Pair<Int, Int>> {
		var total = 0
		return totalOccurrences.keys.sorted().map { char ->
			char to (total to total + (totalOccurrences[char] ?: 0)).also { total += totalOccurrences[char]!! }
		}.toMap()
	}

	private fun findPatternMatches(
		firstColumn: Map<Char, Pair<Int, Int>>,
		rankings: Map<Char, List<Int>>,
		pattern: String
	): Int {
		var (l, r) = firstColumn[pattern.last()] ?: return 0
		for (i in pattern.length - 2 downTo 0) {
			val char = pattern[i]
			l = firstColumn[char]?.first?.plus(rankings[char]?.get(l - 1) ?: 0) ?: 0
			r = firstColumn[char]?.first?.plus(rankings[char]?.get(r - 1) ?: 0) ?: 0
			if (r <= l) return 0
		}
		return r - l
	}

}