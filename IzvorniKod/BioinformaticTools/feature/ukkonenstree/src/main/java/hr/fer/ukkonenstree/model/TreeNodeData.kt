package hr.fer.ukkonenstree.model

import java.util.SortedMap

class TreeNodeData(
	val id: Int,
	val children: SortedMap<Char, TreeNodeData>,
	var suffixLink: TreeNodeData?,
	val start: Int,
	val end: Int?,
) {

	var requiredHeight: Float = 0f
}