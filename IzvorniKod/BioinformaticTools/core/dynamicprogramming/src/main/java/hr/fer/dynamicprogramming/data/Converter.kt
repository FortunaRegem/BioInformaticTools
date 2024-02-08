package hr.fer.dynamicprogramming.data

import hr.fer.dynamicprogramming.data.model.CellEntityModel
import hr.fer.dynamicprogramming.data.model.CellResultEntityModel
import hr.fer.dynamicprogramming.data.model.IntPairDataModel
import hr.fer.dynamicprogramming.data.model.TableEntityModel
import hr.fer.dynamicprogramming.data.model.TableParametersDataModel
import hr.fer.dynamicprogramming.data.model.TableWithCellData
import hr.fer.dynamicprogramming.domain.model.CellModel
import hr.fer.dynamicprogramming.domain.model.CellResultModel
import hr.fer.dynamicprogramming.domain.model.TableModel
import hr.fer.dynamicprogramming.domain.model.TableParametersModel

fun TableModel.toEntityModel(type: Int) = TableEntityModel(
	tableId = "${this.tableParameters.sequenceA}-${this.tableParameters.sequenceB}-${type}".hashCode(),
	tableParameters = this.tableParameters.toDataModel(),
	minimumDistance = this.minimumDistance,
	scorePosition = IntPairDataModel(this.scorePosition),
	dateCreated = this.dateCreated,
	type = type
)

fun TableParametersModel.toDataModel() = TableParametersDataModel(
	sequenceA = this.sequenceA,
	sequenceB = this.sequenceB,
	rowCount = this.rowCount,
	columnCount = this.columnCount,
	match = this.match,
	mismatch = this.mismatch,
	gap = this.gap,
	distance = this.distance
)

fun CellModel.toEntityModel(tableReferenceId: Int) = CellEntityModel(
	cellId = 0,
	value = this.value,
	position = IntPairDataModel(this.position),
	tableReferenceId = tableReferenceId
)

fun CellResultModel.toEntityModel(cellReferenceId: Int) = CellResultEntityModel(
	id = 0,
	predecessor = IntPairDataModel(this.predecessor),
	seqA = this.seqA,
	result = this.result,
	seqB = this.seqB,
	cellReferenceId = cellReferenceId
)

fun TableWithCellData.toModel() = this.table!!.toModel(
	tableData = this.tableData?.map { it.row!!.toModel(it.valuePredecessors!!.map { item -> item.toModel() }) } ?: arrayListOf()
)

fun TableEntityModel.toModel(tableData: List<CellModel>) = TableModel(
	tableData = tableData.toTypedArray(),
	tableParameters = this.tableParameters!!.toModel(),
	minimumDistance = this.minimumDistance,
	scorePosition = this.scorePosition.getPair(),
	dateCreated = this.dateCreated
)

fun TableParametersDataModel.toModel() = TableParametersModel(
	sequenceA = this.sequenceA,
	sequenceB = this.sequenceB,
	rowCount = this.rowCount,
	columnCount = this.columnCount,
	match = this.match,
	mismatch = this.mismatch,
	gap = this.gap,
	distance = this.distance
)

fun CellEntityModel.toModel(valuePredecessors: List<CellResultModel>) = CellModel(
	value = this.value,
	position = this.position?.getPair() ?: Pair(0, 0),
	valuePredecessors = valuePredecessors as ArrayList<CellResultModel>
)

fun CellResultEntityModel.toModel() = CellResultModel(
	predecessor = this.predecessor?.getPair() ?: Pair(0, 0),
	seqA = this.seqA,
	result = this.result,
	seqB = this.seqB
)