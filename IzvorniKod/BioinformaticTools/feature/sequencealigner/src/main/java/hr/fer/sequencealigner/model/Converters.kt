package hr.fer.sequencealigner.model

import hr.fer.common.models.BindableItem
import hr.fer.common.models.sequencealigner.AlignmentResultItem
import hr.fer.common.models.sequencealigner.TableItem
import hr.fer.sequencealigner.R
import hr.fer.sequencealigner.BR

fun AlignmentResultItem.toBindableItem() = BindableItem(
	data = this,
	variableId = BR.alignmentResult,
	layoutId = R.layout.item_alignment_result,
)

fun TableItem.toBindableItem() = BindableItem(
	data = this,
	variableId = BR.tableData,
	layoutId = R.layout.item_history
)