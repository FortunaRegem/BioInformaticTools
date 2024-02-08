package hr.fer.fmindex.data

import hr.fer.fmindex.data.model.FMIndexEntityModel
import hr.fer.fmindex.domain.model.FMIndexModel

fun FMIndexModel.toEntityModel() = FMIndexEntityModel(
	fmIndexId = "${this.sequence.replace("$", "")}${this.pattern}".hashCode(),
	sequence = this.sequence,
	pattern = this.pattern,
	suffixArray = this.suffixArray,
	transformedStringBWT = this.transformedStringBWT,
	firstString = this.firstString,
	charRankings = this.charRankings,
	resultsCount = this.resultsCount,
	dateCreated = this.dateCreated,
)

fun FMIndexEntityModel.toModel() = FMIndexModel(
	sequence = this.sequence,
	pattern = this.pattern,
	suffixArray = this.suffixArray,
	transformedStringBWT = this.transformedStringBWT,
	firstString = this.firstString,
	charRankings = this.charRankings,
	resultsCount = this.resultsCount,
	dateCreated = this.dateCreated,
)

//fun FMIndexStepEntityModel.toModel() = FMIndexStepModel(
//
//)