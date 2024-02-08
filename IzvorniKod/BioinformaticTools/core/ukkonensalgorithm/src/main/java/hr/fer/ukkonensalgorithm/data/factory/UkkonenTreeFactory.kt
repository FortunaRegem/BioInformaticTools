package hr.fer.ukkonensalgorithm.data.factory

import hr.fer.ukkonensalgorithm.domain.contract.UkkonenCalculatorFactoryContract
import hr.fer.ukkonensalgorithm.domain.model.SuffixTreeModel
import hr.fer.ukkonensalgorithm.domain.model.TreeNodeModel
import timber.log.Timber
import java.util.SortedMap
import java.util.TreeMap

class UkkonenTreeFactory : UkkonenCalculatorFactoryContract {

	private lateinit var suffixTree: SuffixTreeModel

	private var suffixTrees = arrayListOf<SuffixTreeModel>()
	private var nodeCounter = 0

	override fun findUkkonenWithSteps(sequence: String): ArrayList<SuffixTreeModel> {
		suffixTree = SuffixTreeModel(sequence)
		suffixTree.root.apply {
			id = nodeCounter++
		}

		cloneTree()

		for (i in sequence.indices) {
			startNewPhase(i)
		}

		return suffixTrees
	}

	private fun cloneTree() {
		suffixTrees.add(
			suffixTree.clone()
		)
	}

	private fun startNewPhase(pos: Int) {
		suffixTree.position = pos
		suffixTree.remainingSuffixCount++
		suffixTree.lastNewNode = null

		while (suffixTree.remainingSuffixCount > 0) {
			if (suffixTree.activeLength == 0) suffixTree.activeEdge = suffixTree.strSequence[pos]

			val activeEdgeChar = suffixTree.activeEdge ?: continue
			val nextNode = suffixTree.activeNode.children[activeEdgeChar]

			if (nextNode == null) {
				// Rule 2 (New leaf)
				suffixTree.activeNode.children[activeEdgeChar] = TreeNodeModel().apply {
					start = pos
					end = null
					id = nodeCounter++
				}
				if (suffixTree.activeNode.id != 0) {
					addSuffixLink(suffixTree.activeNode)
				}
			} else {
				// Rule 3 (Extension)
				if (walkDown(nextNode)) continue
				if (suffixTree.strSequence[nextNode.start + suffixTree.activeLength] == suffixTree.strSequence[pos]) {
					suffixTree.activeLength++
					if (suffixTree.activeNode.id != 0) {
						addSuffixLink(suffixTree.activeNode)
					}
					cloneTree()
					break
				}

				// Rule 2 (New internal and leaf)
				val splitEnd = nextNode.start + suffixTree.activeLength
				val splitNode = TreeNodeModel().apply {
					start = nextNode.start
					end = splitEnd
					id = nodeCounter++
					children[suffixTree.strSequence[pos]] = TreeNodeModel().apply {
						start = pos
						end = null
						id = nodeCounter++
					}
					children[suffixTree.strSequence[nextNode.start + suffixTree.activeLength]] = nextNode.apply {
						start += suffixTree.activeLength
					}
				}
				suffixTree.activeNode.children[activeEdgeChar] = splitNode
				addSuffixLink(splitNode)
			}

			suffixTree.remainingSuffixCount--

			if (suffixTree.activeNode == suffixTree.root && suffixTree.activeLength > 0) {
				suffixTree.activeLength--
				suffixTree.activeEdge = suffixTree.strSequence[pos - suffixTree.remainingSuffixCount + 1]
			} else {
				suffixTree.activeNode = suffixTree.activeNode.suffixLink ?: suffixTree.root
			}

			cloneTree()
		}
	}

	private fun walkDown(nextNode: TreeNodeModel): Boolean {
		if (suffixTree.activeLength >= nextNode.edgeLength(suffixTree.position)) {
			suffixTree.activeEdge = suffixTree.strSequence[nextNode.start + nextNode.edgeLength(suffixTree.position)]
			suffixTree.activeLength -= nextNode.edgeLength(suffixTree.position)
			suffixTree.activeNode = nextNode
			return true
		}
		return false
	}

	private fun addSuffixLink(node: TreeNodeModel) {
		suffixTree.lastNewNode?.suffixLink = node
		suffixTree.lastNewNode = node
	}
}