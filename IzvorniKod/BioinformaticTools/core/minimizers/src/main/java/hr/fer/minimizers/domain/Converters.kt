package hr.fer.minimizers.domain

import hr.fer.minimizers.domain.model.MinimizerStepModel
import hr.fer.minimizers.domain.model.MinimizersModel
import hr.fer.minimizertable.model.MinimizerStepData
import hr.fer.minimizertable.model.MinimizersTableData
import hr.fer.minimizertable.model.MinimizersTableHistoryData

fun MinimizersModel.toData() = MinimizersTableData(
	sequence = this.sequence,
	w = this.w,
	k = this.k,
	maxWindowRow = this.maxWindowsRow,
	steps = this.steps.map { it.toData() } as ArrayList<MinimizerStepData>,
)

fun MinimizersModel.toHistoryData() = MinimizersTableHistoryData(
	sequence = this.sequence,
	w = this.w,
	k = this.k,
	dateCreated = this.dateCreated,
)

fun MinimizerStepModel.toData() = MinimizerStepData(
	currentStep = this.currentStep,
	currentMinKmerStartPos = this.currentMinKmerStartPos,
	nextKmerStartPos = this.nextKmerStartPos,
	windowResultStartPos = this.windowResultStartPos,
	hasNextKmer = this.hasNextKmer,
	currentWindowRow = this.currentWindowRow,
	currentWindowStartPos = this.currentWindowStartPos,
	currentWindow = this.currentWindow,
)