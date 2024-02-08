package hr.fer.minimizers.data

import hr.fer.minimizers.data.model.MinimizerStepEntityModel
import hr.fer.minimizers.data.model.MinimizerWithStepData
import hr.fer.minimizers.data.model.MinimizersEntityModel
import hr.fer.minimizers.domain.model.MinimizerStepModel
import hr.fer.minimizers.domain.model.MinimizersModel

fun MinimizersModel.toEntityModel() = MinimizersEntityModel(
	minimizerId = "${this.sequence}${this.w}${this.k}".hashCode(),
	sequence = this.sequence,
	w = this.w,
	k = this.k,
	maxWindowsRow = this.maxWindowsRow,
	dateCreated = this.dateCreated
)

fun MinimizerStepModel.toEntityModel(minimizerId: Int) = MinimizerStepEntityModel(
	minimizerStepId = 0,
	minimizerReferenceId = minimizerId,
	currentStep = this.currentStep,
	currentMinKmerStartPos = this.currentMinKmerStartPos,
	nextKmerStartPos = this.nextKmerStartPos,
	windowResultStartPos = this.windowResultStartPos,
	hasNextKmer = this.hasNextKmer,
	currentWindowRow = this.currentWindowRow,
	currentWindowStartPos = this.currentWindowStartPos,
	currentWindow = this.currentWindow,
)

fun MinimizerWithStepData.toModel() = this.minimizer!!.toModel(
	this.minimizerSteps?.map { it.toModel() } as ArrayList<MinimizerStepModel>
)

fun MinimizersEntityModel.toModel(steps: ArrayList<MinimizerStepModel>) = MinimizersModel(
	sequence = this.sequence,
	w = this.w,
	k = this.k,
	steps = steps,
	maxWindowsRow = this.maxWindowsRow,
	dateCreated = this.dateCreated
)

fun MinimizerStepEntityModel.toModel() = MinimizerStepModel(
	currentStep = this.currentStep,
	currentMinKmerStartPos = this.currentMinKmerStartPos,
	nextKmerStartPos = this.nextKmerStartPos,
	windowResultStartPos = this.windowResultStartPos,
	hasNextKmer = this.hasNextKmer,
	currentWindowRow = this.currentWindowRow,
	currentWindowStartPos = this.currentWindowStartPos,
	currentWindow = this.currentWindow,
)