package hr.fer.ukkonensalgorithm.domain.model

class SuffixTreeModel(
	val strSequence: String
) {
	var root = TreeNodeModel()
	var activeNode = root
	var activeEdge: Char? = null
	var activeLength = 0
	var remainingSuffixCount = 0
	var lastNewNode: TreeNodeModel? = null
	var position = -1

	fun clone(): SuffixTreeModel {
		val newTree = SuffixTreeModel(if (position == -1) "" else this.strSequence.substring(0, position + 1))
		val nodeMap = mutableMapOf<TreeNodeModel, TreeNodeModel>()

		newTree.root = cloneNodesAndMap(this.root, nodeMap)
		updateSuffixLinksInClone(nodeMap)

		val oldToNewNodeMap = mutableMapOf<TreeNodeModel, TreeNodeModel>()
		mapOldToNew(this.root, newTree.root, oldToNewNodeMap)

		newTree.activeNode = oldToNewNodeMap[this.activeNode] ?: newTree.root
		newTree.activeEdge = this.activeEdge
		newTree.activeLength = this.activeLength
		newTree.remainingSuffixCount = this.remainingSuffixCount
		newTree.lastNewNode = this.lastNewNode?.let { oldToNewNodeMap[it] }
		newTree.position = this.position

		return newTree
	}

	private fun updateSuffixLinksInClone(map: Map<TreeNodeModel, TreeNodeModel>) {
		map.forEach { (originalNode, clonedNode) ->
			originalNode.suffixLink?.let { suffixLink ->
				clonedNode.suffixLink = map[suffixLink]
			}
		}
	}

	private fun cloneNodesAndMap(originalNode: TreeNodeModel, map: MutableMap<TreeNodeModel, TreeNodeModel>, cloneFull: Boolean = true): TreeNodeModel {
		val clonedNode = if (cloneFull) {
			originalNode.clone()
		} else {
			originalNode.cloneEmpty()
		}
		map[originalNode] = clonedNode

		originalNode.children.forEach { (key, childNode) ->
			clonedNode.children[key] = cloneNodesAndMap(childNode, map, false)
		}

		return clonedNode
	}

	private fun mapOldToNew(oldNode: TreeNodeModel, newNode: TreeNodeModel, map: MutableMap<TreeNodeModel, TreeNodeModel>) {
		map[oldNode] = newNode
		oldNode.children.forEach { (char, oldChild) ->
			newNode.children[char]?.let { newChild ->
				mapOldToNew(oldChild, newChild, map)
			}
		}
	}
}