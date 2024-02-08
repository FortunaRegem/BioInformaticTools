package hr.fer.ukkonensalgorithm.domain.model

import java.util.SortedMap
import java.util.TreeMap

class TreeNodeModel {
	var id: Int = 0
	var children: SortedMap<Char, TreeNodeModel> = TreeMap()
	var suffixLink: TreeNodeModel? = null
	var start: Int = 0
	var end: Int? = null

	fun edgeLength(currentIndex: Int) = (end ?: (currentIndex + 1)) - start

	fun clone(): TreeNodeModel {
		val newNode = TreeNodeModel()
		newNode.id = this.id
		newNode.start = this.start
		newNode.end = this.end

		this.children.forEach { (key, child) ->
			newNode.children[key] = child.clone()
		}

		return newNode
	}

	fun cloneEmpty(): TreeNodeModel {
		val newNode = TreeNodeModel()
		newNode.id = this.id
		newNode.start = this.start
		newNode.end = this.end

		return newNode
	}
}