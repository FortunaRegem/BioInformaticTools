package hr.fer.ukkonenstree.ui.result.view.treeview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import hr.fer.common.R as commonR
import hr.fer.ukkonenstree.model.SuffixTreeData
import hr.fer.ukkonenstree.model.TreeNodeData
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin


class TreeView : View, ScaleGestureDetector.OnScaleGestureListener {

	constructor(context: Context?) : super(context) {
		if (!isInEditMode) {
			init()
		}
	}

	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
		if (!isInEditMode) {
			init()
		}
	}

	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		if (!isInEditMode) {
			init()
		}
	}

	/**
	 * Table drawing data
	 */
	private val nodeRadius: Float = 50f
	private var horizontalSpacing: Float = 300f
	private val verticalSpacing: Float = 150f

	private val textFontSize = 40f
	private val textPaint = Paint().apply {
		color = Color.BLACK
		textSize = textFontSize
		typeface = ResourcesCompat.getFont(context, commonR.font.fira_code_regular)?.let { Typeface.create(it, Typeface.NORMAL) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}

	private val nodePaint = Paint().apply {
		color = Color.rgb(200, 200, 255)
		style = Paint.Style.FILL
	}

	private val nodePaintGreen = Paint().apply {
		color = Color.rgb(200,255,200)
		style = Paint.Style.FILL
	}
	private val nodePaintRed = Paint().apply {
		color = Color.rgb(255,200,200)
		style = Paint.Style.FILL
	}

	private val borderPaint = Paint().apply {
		color = Color.rgb(150, 150, 200)
		style = Paint.Style.STROKE
		strokeWidth = 4f
	}

	private val borderPaintGreen = Paint().apply {
		color = Color.rgb(150, 200, 150)
		style = Paint.Style.STROKE
		strokeWidth = 4f
	}

	private val borderPaintRed = Paint().apply {
		color = Color.rgb(200, 150, 150)
		style = Paint.Style.STROKE
		strokeWidth = 4f
	}

	private val linePaint = Paint().apply {
		color = Color.BLACK
		strokeWidth = 3f
	}

	private val arrowPaint = Paint().apply {
		color = Color.BLACK
		strokeWidth = 2f
		style = Paint.Style.FILL_AND_STROKE
		isAntiAlias = true
	}
	private val arrowPaintDashed = Paint().apply {
		color = Color.GRAY
		strokeWidth = 2f
		style = Paint.Style.STROKE
		isAntiAlias = true
		pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
	}

	private lateinit var treeDrawingData: ArrayList<SuffixTreeData>
	private var nodes: ArrayList<AnimatedNodeWithPositionData> = arrayListOf()
	private var currentStepPosition: Int = -1

	var activeNode: String = ""
	var activeEdge: String = ""
	var activeLength: String = ""
	var reminder: String = ""


	private val animationPlayHandler: Handler = Handler(Looper.getMainLooper())
	private lateinit var nodeDrawing: Runnable
	private lateinit var offsetDrawing: Runnable
	private var isRemoving: Boolean = false


	private fun init() {
		scaleGestureDetector = ScaleGestureDetector(context, this)
		scaleFactor = 1f

		nodeDrawing = Runnable {
			var needNewFrame = false
			val startTime = System.currentTimeMillis()

			for (node in nodes) {
				if (node.updatePosition(startTime)) {
					needNewFrame = true
				}
			}

			if (needNewFrame) {
				handler.postDelayed(nodeDrawing, (11 - (System.currentTimeMillis() - startTime)).coerceAtLeast(0))
			} else {
				nodes.removeIf {
					it.markRemove
				}
				isRemoving = false
			}

			invalidate()
		}

		offsetDrawing = Runnable {
			val startTime = System.currentTimeMillis()

			val needNewFrame = updateOffsets(startTime)
			println(needNewFrame.toString())

			if (needNewFrame) {
				handler.postDelayed(offsetDrawing, (11 - (System.currentTimeMillis() - startTime)).coerceAtLeast(0))
			}
		}
	}



	private var left: Float = 0f
	private var top: Float = 0f
	private var right: Float = 0f
	private var bottom: Float = 0f

	private fun calculateNewBorders() {
		left = -xOffset - nodeRadius
		top = -yOffset - nodeRadius
		right = left + width + nodeRadius
		bottom = top + height + nodeRadius
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		canvas.save()
		canvas.scale(scaleFactor, scaleFactor)
		canvas.translate(xOffset, yOffset)

		calculateNewBorders()
		if (this::treeDrawingData.isInitialized) {
			if (isRemoving) {
				drawTree(treeDrawingData[currentStepPosition + 1].root, canvas)
			} else {
				drawTree(treeDrawingData[currentStepPosition].root, canvas)
			}
		}

		canvas.restore()
	}

	private fun drawTree(node: TreeNodeData, canvas: Canvas, parentX: Float? = null, parentY: Float? = null) {
		val currentNode = nodes.find { it.nodeId == node.id } ?: return

		val x = currentNode.currentPositionX
		val y = currentNode.currentPositionY


		if (parentX != null && parentY != null) {
			val angle = atan2(y - parentY, x - parentX)
			val lineX = parentX + nodeRadius * cos(angle)
			val lineY = parentY + nodeRadius * sin(angle)
			canvas.drawLine(lineX, lineY, x, y, linePaint)

			val midX = (parentX + x) / 2
			val midY = (parentY + y) / 2
			val textAngle = Math.toDegrees(angle.toDouble()).toFloat()

			val save = canvas.save()

			canvas.rotate(textAngle, midX, midY)
			canvas.drawText(currentNode.nodeText ?: "", midX, midY + (textPaint.ascent() + textPaint.descent()) * 0.8f, textPaint)

			canvas.restoreToCount(save)

		}

		if (node.suffixLink != null) {
			val suffixNode = nodes.find { node.suffixLink!!.id == it.nodeId }
			if (suffixNode != null) {
				val deltaX = x - suffixNode.currentPositionX
				val deltaY = y - suffixNode.currentPositionY
				val angleArrow = atan2(deltaY, deltaX)

				val endX = suffixNode.currentPositionX + nodeRadius * cos(angleArrow)
				val endY = suffixNode.currentPositionY + nodeRadius * sin(angleArrow)
				drawArrow(canvas, x, y, endX, endY)
			}
		}

		canvas.drawCircle(x, y, nodeRadius, if (currentNode.isNodeActive) nodePaintRed else nodePaint)
		canvas.drawCircle(x, y, nodeRadius, if (currentNode.isNodeActive) borderPaintRed else borderPaint)

		canvas.drawText(node.id.toString(), x, y + (textPaint.textSize / 3), textPaint)

		node.children.forEach { (_, child) ->
			drawTree(child, canvas, x, y)
		}
	}

	private fun drawArrow(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float) {
		val midX = (startX + endX) / 2
		val midY = (startY + endY) / 2
		val controlX = midX + (endY - startY) * 0.2f
		val controlY = midY - (endX - startX) * 0.2f

		val path = Path().apply {
			moveTo(startX, startY)
			quadTo(controlX, controlY, endX, endY)
		}

		canvas.drawPath(path, arrowPaintDashed)

		val dx = (2 * (endX - controlX))
		val dy = (2 * (endY - controlY))
		val angle = atan2(dy.toDouble(), dx.toDouble())

		val arrowHeadLength = 20f
		val arrowAngle = Math.toRadians(20.0)

		val x1 = endX - arrowHeadLength * cos(angle - arrowAngle)
		val y1 = endY - arrowHeadLength * sin(angle - arrowAngle)
		val x2 = endX - arrowHeadLength * cos(angle + arrowAngle)
		val y2 = endY - arrowHeadLength * sin(angle + arrowAngle)

		path.reset()
		path.moveTo(endX, endY)
		path.lineTo(x1.toFloat(), y1.toFloat())
		path.lineTo(x2.toFloat(), y2.toFloat())
		path.lineTo(endX, endY)

		canvas.drawPath(path, arrowPaint)
	}

	/**
	 * User animation control functions
	 */
	fun nextStep() {
		animationPlayHandler.removeCallbacks(nodeDrawing)
		currentStepPosition++
		if (currentStepPosition >= treeDrawingData.size) {
			currentStepPosition = treeDrawingData.size - 1
			return
		}
		calculateNextTree()
		animationPlayHandler.postDelayed(nodeDrawing, 11)
		invalidate()
	}
	fun previousStep() {
		animationPlayHandler.removeCallbacks(nodeDrawing)
		currentStepPosition--
		if (currentStepPosition <= -1) {
			currentStepPosition = 0
			return
		}
		isRemoving = true
		calculateNextTree(true)
		animationPlayHandler.postDelayed(nodeDrawing, 11)
		invalidate()
	}

	/**
	 * Handling all panning and zooming on canvas
	 **/
	private lateinit var scaleGestureDetector: ScaleGestureDetector
	private var scaleFactor = 1f
	private var xOffset = nodeRadius
	private var yOffset = nodeRadius

	private var nextXOffset = nodeRadius
	private var nextYOffset = nodeRadius
	var startTime: Long = -1L


	private var totalWidth: Float = 0f
	private var totalHeight: Float = 0f

	private fun updateOffsets(elapsedTime: Long): Boolean {
		if (startTime < 0L) {
			startTime = elapsedTime
		}

		val progress = ((elapsedTime - startTime) / ANIMATION_DURATION.toFloat()).coerceIn(0f, 1f)

		val interpolation = easeInOutCubic(progress)

		val targetX = (nextXOffset - xOffset) * interpolation
		val targetY = (nextYOffset - yOffset) * interpolation

		xOffset += targetX
		yOffset += targetY

		println(yOffset.toString())

		val isAnimationCompleted = (nextXOffset - xOffset).absoluteValue < POSITION_THRESHOLD &&
				(nextYOffset - yOffset).absoluteValue < POSITION_THRESHOLD

		if (isAnimationCompleted) {
			startTime = -1L
		}

		return !isAnimationCompleted
	}

	override fun onScale(detector: ScaleGestureDetector): Boolean {
		scaleFactor *= detector.scaleFactor
		scaleFactor = max(-10f, min(scaleFactor, 3.0f))
		invalidate()
		return true
	}

	override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
		return true
	}

	override fun onScaleEnd(detector: ScaleGestureDetector) {
	}

	private var prevX: Float = 0f
	private var prevY: Float = 0f

	override fun onTouchEvent(e: MotionEvent): Boolean {
		scaleGestureDetector.onTouchEvent(e)

		when (e.action) {
			MotionEvent.ACTION_DOWN -> {
				prevX = e.x
				prevY = e.y
			}

			MotionEvent.ACTION_MOVE -> {
				if (!scaleGestureDetector.isInProgress) {
					val dx = e.x - prevX
					val dy = e.y - prevY

					println("++++ animation removed")
					animationPlayHandler.removeCallbacksAndMessages(offsetDrawing)
					startTime = -1L

					xOffset += dx / scaleFactor
					yOffset += dy / scaleFactor

					if (this::treeDrawingData.isInitialized) {
						val visibleWidth = width / scaleFactor
						val visibleHeight = height / scaleFactor

						val maxXOffset = max(0f, totalWidth - visibleWidth)
						val maxYOffset = max(0f, (totalHeight + (nodeRadius * 8 / scaleFactor)) - visibleHeight)

						xOffset = max(-maxXOffset, min(xOffset, nodeRadius))
						yOffset = max(-maxYOffset, min(yOffset, nodeRadius))

						println(yOffset.toString())

						invalidate()
					}
				}

				prevX = e.x
				prevY = e.y
			}
		}
		return true
	}

	private fun TreeNodeData.treeDepth(): Int {
		if (this.children.isEmpty()) return 1
		return 1 + this.children.values.maxOf { it.treeDepth() }
	}

	private fun TreeNodeData.calculateHeightRequired(): Float {
		if (this.children.isEmpty()) {
			this.requiredHeight = 2 * nodeRadius
		} else {
			this.requiredHeight = this.children.values.map { child ->
				child.calculateHeightRequired()
			}.sum() + (this.children.size - 1) * verticalSpacing
		}
		return this.requiredHeight
	}

	private fun calculateRequiredSpace(root: TreeNodeData): Pair<Float, Float> {
		val depth = root.treeDepth()

		val calcWidth = (depth * nodeRadius) + (depth - 1) * horizontalSpacing
		val totalWidth = (if (width > calcWidth) width.toFloat() else calcWidth) + (nodeRadius * 2)
		val totalHeight = root.calculateHeightRequired()

		return Pair(totalWidth, totalHeight)
	}

	private fun calculateSteps() {
		horizontalSpacing = max(textPaint.measureText(treeDrawingData.last().strSequence) * 1.2f, horizontalSpacing)
		val (width, height) = calculateRequiredSpace(treeDrawingData.last().root)

		totalWidth = width
		totalHeight = height

		currentStepPosition++
		calculateNextTree()
	}

	private fun calculateNextTree(hasReduced: Boolean = false) {
		val tree = treeDrawingData[currentStepPosition]
		if (hasReduced) {
			val oldIds = mutableSetOf<Int>()
			val newIds = mutableSetOf<Int>()
			findAllNodeIds(treeDrawingData[currentStepPosition + 1].root, oldIds)
			findAllNodeIds(tree.root, newIds)

			val idsToRemove = ((oldIds union newIds) subtract (oldIds intersect newIds))

			nodes.forEach {
				if (it.nodeId in idsToRemove) {
					it.markRemove()
				}
			}
		}

		activeNode = tree.activeNode?.id?.toString() ?: "0"
		activeEdge = tree.activeEdge?.toString() ?: "none"
		activeLength = tree.activeLength.toString()
		reminder = tree.remainingSuffixCount.toString()

		layoutTree(tree.root,null, null, 0f, totalHeight, 0, tree.strSequence, tree.activeNode?.id ?: 0)
	}

	private fun findAllNodeIds(node: TreeNodeData, idList: MutableSet<Int>) {
		idList.add(node.id)

		node.children.values.forEach {
			findAllNodeIds(it, idList)
		}

	}

	private fun layoutTree(
		node: TreeNodeData,
		parentX: Float?,
		parentY: Float?,
		totalHeightTop: Float,
		totalHeightBottom: Float,
		level: Int,
		currentSequence: String,
		activeNode: Int
	) {
		val x = nodeRadius + level * horizontalSpacing
		val y = (totalHeightBottom + totalHeightTop) / 2

		var currentNode = nodes.find { it.nodeId == node.id }
		if (currentNode != null) {
			currentNode.drawPositionX = x
			currentNode.drawPositionY = y
			currentNode.isNodeActive = currentNode.nodeId == activeNode

			currentNode.nodeText = currentSequence.substring(node.start, node.end ?: currentSequence.length)

			currentNode.startTime = -1L
		} else {
			currentNode = AnimatedNodeWithPositionData(
				node.id,
				currentSequence.substring(node.start, node.end ?: currentSequence.length),
				parentX ?: x,
				parentY ?: y
			).apply {
				drawPositionX = x
				drawPositionY = y

				currentPositionX = parentX ?: x
				currentPositionY = parentY ?: y

				isNodeActive = nodeId == activeNode
			}

			nodes.add(
				currentNode
			)

			val visibleWidth = width / scaleFactor
			val visibleHeight = height / scaleFactor

			val maxXOffset = max(0f, totalWidth - visibleWidth)
			val maxYOffset = max(0f, (totalHeight + (nodeRadius + (5 / scaleFactor))) - visibleHeight)

			val targetCenterX = x * scaleFactor - visibleHeight
			val targetCenterY = y * scaleFactor - visibleHeight / 2f

			nextXOffset = -targetCenterX.coerceIn(-nodeRadius, maxXOffset)
			nextYOffset = -targetCenterY.coerceIn(-nodeRadius, maxYOffset)


			println("---- $y")
			println("---- $nextYOffset")

			println("++++ animation removed and added")
			animationPlayHandler.removeCallbacksAndMessages(offsetDrawing)
			animationPlayHandler.postDelayed(offsetDrawing, 11)
		}

		val nodeTotalHeight = totalHeightBottom - totalHeightTop
		var currentHeightStart = totalHeightTop
		val totalChildren = node.children.values.sumOf { max(it.children.size, 1) }

		node.children.values.forEach { child ->
			val childHeightStart = currentHeightStart
			val childHeightEnd = childHeightStart + (
				if (totalChildren == 0) 1f / node.children.size * nodeTotalHeight
				else max(child.children.size, 1).toFloat() / totalChildren * nodeTotalHeight
			)

			currentHeightStart = childHeightEnd

			layoutTree(
				child,
				currentNode.currentPositionX,
				currentNode.currentPositionY,
				childHeightStart,
				childHeightEnd,
				level + 1,
				currentSequence,
				activeNode
			)
		}
	}

	companion object {
		const val ANIMATION_DURATION = 1500L
		const val POSITION_THRESHOLD: Float = 0.01f

		@JvmStatic
		@BindingAdapter("android:treeData")
		fun TreeView.setTableData(treeData: ArrayList<SuffixTreeData>?) {
			if (treeData == null) return

			this.treeDrawingData = treeData
			calculateSteps()

			invalidate()
		}

		fun easeInOutCubic(t: Float): Float {
			return if (t < 0.5f) {
				4 * t * t * t
			} else {
				1 - (-2 * t + 2).pow(3) / 2
			}
		}
	}

	private class AnimatedNodeWithPositionData(
		val nodeId: Int,
		var nodeText: String?,
		val originalPositionX: Float,
		val originalPositionY: Float,
	) {

		var drawPositionX: Float = 0f
		var drawPositionY: Float = 0f

		var currentPositionX: Float = 0f
		var currentPositionY: Float = 0f

		var isNodeActive: Boolean = false
		var markRemove: Boolean = false

		var startTime: Long = -1L

		fun updatePosition(elapsedTime: Long): Boolean {
			if (startTime < 0L) {
				startTime = elapsedTime
			}

			val progress = ((elapsedTime - startTime) / ANIMATION_DURATION.toFloat()).coerceIn(0f, 1f)

			val interpolation = easeInOutCubic(progress)

			val targetX = (drawPositionX - currentPositionX) * interpolation
			val targetY = (drawPositionY - currentPositionY) * interpolation

			currentPositionX += targetX
			currentPositionY += targetY

			val isAnimationCompleted = (drawPositionX - currentPositionX).absoluteValue < POSITION_THRESHOLD &&
					(drawPositionY - currentPositionY).absoluteValue < POSITION_THRESHOLD

			if (isAnimationCompleted) {
				startTime = -1L
			}

			return !isAnimationCompleted
		}

		fun markRemove() {
			markRemove = true
			startTime = -1
			drawPositionX = originalPositionX
			drawPositionY = originalPositionY
		}
	}
}