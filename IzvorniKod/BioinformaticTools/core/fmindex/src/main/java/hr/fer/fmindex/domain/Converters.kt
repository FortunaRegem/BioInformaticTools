package hr.fer.fmindex.domain

import hr.fer.fmindex.domain.model.FMIndexModel
import hr.fer.fmindextable.model.FMIndexTableData
import hr.fer.fmindextable.model.FMIndexTableHistoryData

fun FMIndexModel.toHistoryData() = FMIndexTableHistoryData(
	sequence = this.sequence,
	pattern = this.pattern,
	dateCreated = this.dateCreated
)

fun FMIndexModel.toData() = FMIndexTableData(
	sequence = this.sequence,
	pattern = this.pattern,
	suffixArray = this.suffixArray,
	transformedStringBWT = this.transformedStringBWT,
	firstString = this.firstString,
	charRankings = this.charRankings,
	resultsCount = this.resultsCount,
	dateCreated = this.dateCreated,
)