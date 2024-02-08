package hr.fer.ukkonensalgorithm.domain.contract

import hr.fer.ukkonensalgorithm.domain.model.SuffixTreeModel

interface UkkonenCalculatorFactoryContract {
	fun findUkkonenWithSteps(sequence: String): ArrayList<SuffixTreeModel>
}