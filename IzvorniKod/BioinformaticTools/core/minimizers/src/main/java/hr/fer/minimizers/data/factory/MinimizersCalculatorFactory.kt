package hr.fer.minimizers.data.factory

import hr.fer.minimizers.domain.contract.MinimizersCalculatorFactoryContract
import hr.fer.minimizers.domain.model.MinimizerStepModel
import hr.fer.minimizers.domain.model.MinimizersModel

class MinimizersCalculatorFactory : MinimizersCalculatorFactoryContract {

	override fun findMinimizersWithSteps(sequence: String, w: Int, k: Int): MinimizersModel {
		val steps: ArrayList<MinimizerStepModel> = arrayListOf()

		val maxWindowLength = w + k - 1

		var currentWindowLength = w
		var currentWindowPosition = 0
		var currentWindowRow = 1

		var latestMinStartPosition = 0
		var currentStep = 0

		while(currentWindowPosition <= sequence.length - w) {

			val currentWindow = sequence.substring(currentWindowPosition, currentWindowPosition + currentWindowLength)

			var currentMinStartPosition = if (latestMinStartPosition >= currentWindowPosition) latestMinStartPosition else currentWindowPosition

			val loopStart = currentMinStartPosition + 1
			var loopEnd = (currentWindowPosition + currentWindowLength) - k
			loopEnd = if (loopEnd < loopStart) loopStart else loopEnd

			for (i in loopStart..loopEnd) {
				val currentKmer = sequence.substring(currentMinStartPosition, currentMinStartPosition + k)
				val nextKmer = if (loopStart == loopEnd && (loopEnd == 1 || loopEnd == sequence.length - k + 1) ) {
					sequence.substring(currentMinStartPosition, currentMinStartPosition + k)
				} else {
					sequence.substring(i, i + k)
				}

				val step = MinimizerStepModel(
					currentStep = currentStep,
					currentMinKmerStartPos = currentMinStartPosition,
					nextKmerStartPos = i,
					windowResultStartPos = if (nextKmer < currentKmer) i else currentMinStartPosition,
					hasNextKmer = i < loopEnd,
					currentWindowRow = currentWindowRow,
					currentWindowStartPos = currentWindowPosition,
					currentWindow = currentWindow,
				)

				steps.add(step)
				currentStep++

				if (nextKmer < currentKmer) {
					currentMinStartPosition = i
				}
			}

			latestMinStartPosition = currentMinStartPosition
			currentWindowRow++

			if (currentWindowLength == maxWindowLength && currentWindowPosition + currentWindowLength < sequence.length) {
				currentWindowPosition++
			} else if (currentWindowPosition + currentWindowLength >= sequence.length) {
				currentWindowPosition++
				currentWindowLength--
			} else if (currentWindowLength < maxWindowLength) {
				currentWindowLength++
			}

		}

		return MinimizersModel(
			sequence = sequence,
			w = w,
			k = k,
			steps = steps,
			maxWindowsRow = currentWindowRow - 1,
			dateCreated = System.currentTimeMillis()
		)
	}

}