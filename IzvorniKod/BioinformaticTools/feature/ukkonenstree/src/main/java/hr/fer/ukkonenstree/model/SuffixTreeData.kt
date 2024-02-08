package hr.fer.ukkonenstree.model

class SuffixTreeData(
	val strSequence: String,
	val root: TreeNodeData,
	val activeNode: TreeNodeData?,
	val activeEdge: Char?,
	val activeLength: Int,
	val remainingSuffixCount: Int,
	val lastNewNode: TreeNodeData?,
	val position: Int,
)