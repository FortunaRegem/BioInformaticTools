package hr.fer.fmindex.domain.contract

import hr.fer.fmindex.domain.model.FMIndexModel

interface FMIndexCalculatorFactoryContract {
	fun findFMIndexWithSteps(sequence: String, pattern: String): FMIndexModel
}