package hr.fer.ukkonensalgorithm.domain

import hr.fer.ukkonensalgorithm.domain.model.SuffixTreeModel
import hr.fer.ukkonensalgorithm.domain.model.TreeNodeModel
import hr.fer.ukkonenstree.model.SuffixTreeData
import hr.fer.ukkonenstree.model.TreeNodeData
import java.util.TreeMap

fun SuffixTreeModel.toData(): SuffixTreeData {
	val nodeMap: MutableMap<TreeNodeModel, TreeNodeData> = mutableMapOf()
	val clonedRoot = this.root.toData(nodeMap)

	nodeMap.forEach { (original, cloned) ->
		cloned.suffixLink = original.suffixLink?.let { nodeMap[it] }
	}

	return SuffixTreeData(
		strSequence = this.strSequence,
		root = clonedRoot,
		activeNode = nodeMap[this.activeNode] ?: this.activeNode.toData(nodeMap),
		activeEdge = this.activeEdge,
		activeLength = this.activeLength,
		remainingSuffixCount = this.remainingSuffixCount,
		lastNewNode = this.lastNewNode?.let { nodeMap[it] ?: it.toData(nodeMap) },
		position = this.position
	)
}

fun TreeNodeModel.toData(nodeMap: MutableMap<TreeNodeModel, TreeNodeData>): TreeNodeData {
	return nodeMap.getOrPut(this) {
		TreeNodeData(
			id = this.id,
			children = TreeMap(this.children.mapValues { (_, child) ->
				nodeMap.getOrPut(child) { child.toData(nodeMap) }
			}),
			suffixLink = null,
			start = this.start,
			end = this.end
		)
	}
}