package hr.fer.minimizers.domain.contract

import hr.fer.minimizers.domain.model.MinimizersModel

interface MinimizersCalculatorFactoryContract {
	fun findMinimizersWithSteps(sequence: String, w: Int, k: Int): MinimizersModel
}