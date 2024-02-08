package hr.fer.dynamicprogramming.domain

import hr.fer.common.models.sequencealigner.CellItem
import hr.fer.common.models.sequencealigner.CellResultItem
import hr.fer.common.models.sequencealigner.TableItem
import hr.fer.common.models.sequencealigner.TableParametersItem
import hr.fer.dynamicprogramming.domain.model.CellModel
import hr.fer.dynamicprogramming.domain.model.CellResultModel
import hr.fer.dynamicprogramming.domain.model.TableModel
import hr.fer.dynamicprogramming.domain.model.TableParametersModel

fun TableModel.toItem() = TableItem(
	tableData = tableData.toList().map { it.toItem() }.toTypedArray(),
	tableParameters = this.tableParameters.toItem(),
	minimumDistance = this.minimumDistance,
	scorePosition = this.scorePosition,
	dateCreated = this.dateCreated
)

fun TableParametersModel.toItem() = TableParametersItem(
	sequenceA = this.sequenceA,
	sequenceB = this.sequenceB,
	rowCount = this.rowCount,
	columnCount = this.columnCount,
	match = this.match,
	mismatch = this.mismatch,
	gap = this.gap,
	distance = this.distance
)

fun CellModel.toItem() = CellItem(
	value = this.value.toString(),
	position = this.position,
	valuePredecessors = valuePredecessors.map { it.toItem() } as ArrayList<CellResultItem>
)

fun CellResultModel.toItem() = CellResultItem(
	predecessor = this.predecessor,
	seqA = this.seqA,
	result = this.result,
	seqB = this.seqB
)


