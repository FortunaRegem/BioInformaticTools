package hr.fer.common.view.connectedtable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import hr.fer.common.R
import hr.fer.common.models.sequencealigner.AlignmentResultItem
import hr.fer.common.models.sequencealigner.CellItem
import hr.fer.common.models.sequencealigner.CellResultItem
import hr.fer.common.models.sequencealigner.TableItem
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin


class ConnectedTableView : View, ScaleGestureDetector.OnScaleGestureListener {

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


	private val tableCellWidth: Float = 100f
	private val tableCellHeight: Float = 100f
	private val tableLineThickness: Float = 3f
	private val textFontSize = 30f
	private val arrowCircleOffsetRadius = 25f
	private val tablePaint = Paint().apply {
		color = Color.DKGRAY
		strokeWidth = tableLineThickness
	}
	private val tableCellPaint = Paint().apply {
		color = Color.WHITE
		style = Paint.Style.FILL
	}
	private val tableCellEdgePaint = Paint().apply {
		color = Color.rgb(239, 231, 223)
		style = Paint.Style.FILL
	}
	private val tableCellMainPathPaint = Paint().apply {
		color = Color.rgb(83,130,186)
		style = Paint.Style.FILL
	}
	private val tableCellHighlightedPaint = Paint().apply {
		color = Color.rgb(154,205,49)
		style = Paint.Style.FILL
	}
	private val tableCellExtensionPaint = Paint().apply {
		color = Color.rgb(245,202,209)
		style = Paint.Style.FILL
	}
	private val textPaint = Paint().apply {
		color = Color.BLACK
		textSize = textFontSize
		typeface = ResourcesCompat.getFont(context, R.font.fira_code_regular)?.let { Typeface.create(it, Typeface.NORMAL) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}
	private val textPaintHighlighted = Paint().apply {
		color = Color.WHITE
		textSize = textFontSize
		typeface = ResourcesCompat.getFont(context, R.font.fira_code_regular)?.let { Typeface.create(it, Typeface.NORMAL) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}
	private val arrowPaint = Paint().apply {
		color = Color.BLACK
		strokeWidth = 4f
		style = Paint.Style.FILL_AND_STROKE
		isAntiAlias = true
	}


	private lateinit var tableData: TableItem
	private val cells: ArrayList<CellData> = arrayListOf()
	private lateinit var currentActiveCell: CellData
	private var currentActiveNeighbours: ArrayList<CellData> = arrayListOf()
	private var currentActiveMainPath: ArrayList<CellData> = arrayListOf()
	lateinit var currentMainPathData: AlignmentResultItem



	private fun init() {
		scaleGestureDetector = ScaleGestureDetector(context, this)
		scaleFactor = 1f

	}

	private var left: Float = 0f
	private var top: Float = 0f
	private var right: Float = 0f
	private var bottom: Float = 0f

	private fun calculateNewBorders() {
		left = -xOffset - tableCellWidth
		top = -yOffset - tableCellWidth
		right = left + width + tableCellWidth
		bottom = top + height + tableCellWidth
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		canvas.save()
		canvas.scale(scaleFactor, scaleFactor)
		canvas.translate(xOffset, yOffset)

		calculateNewBorders()
		drawCells(canvas)
		drawInitialTable(canvas)

		canvas.restore()
	}

	private fun drawInitialTable(canvas: Canvas) {
		if (!this::tableData.isInitialized) return

		val columns = tableData.tableParameters.columnCount + 2
		val rows = tableData.tableParameters.rowCount + 2

		for (i in 0..columns) {
			val startX = i * tableCellWidth
			val startY = 0f
			val endY = rows * tableCellHeight

			if (startX > left && startX < right) {
				canvas.drawLine(startX, startY, startX, endY, tablePaint)
			}

		}
		for (i in 0 .. rows) {
			val startX = 0f
			val startY = i * tableCellHeight
			val endX = columns * tableCellWidth

			if (startY > top && startY < bottom) {
				canvas.drawLine(startX, startY, endX, startY, tablePaint)
			}
		}
	}

	private fun drawCells(canvas: Canvas) {
		for (cell in cells) {
			canvas.drawRect(
				cell.startX,
				cell.startY,
				cell.startX + tableCellWidth,
				cell.startY + tableCellHeight,
				if (cell.canBeHighlighted) {
					if (cell.isHighlighted) {
						tableCellHighlightedPaint
					} else if (cell.isNeighbourHighlighted) {
						tableCellExtensionPaint
					} else if (cell.isOnMainPath()) {
						tableCellMainPathPaint
					} else {
						tableCellPaint
					}
				} else {
					tableCellEdgePaint
				}
			)
			drawText(
				canvas,
				cell.cellData.value,
				((2 * cell.startX) + tableCellWidth) / 2,
				((2 * cell.startY) + tableCellHeight) / 2,
				if (cell.isHighlighted || cell.isOnMainPath()) {
					textPaintHighlighted
				} else {
					textPaint
				}
			)

			if (cell.drawArrowLeft) {
				drawArrow(
					canvas,
					(cell.startX + (tableCellWidth / 2)) - arrowCircleOffsetRadius,
					cell.startY + (tableCellHeight / 2),
					(cell.startX + (tableCellWidth / 2)) - arrowCircleOffsetRadius - (tableCellWidth - 2 * arrowCircleOffsetRadius),
					cell.startY + (tableCellHeight / 2),
					arrowPaint
				)
			}
			if (cell.drawArrowDiagonal) {
				drawArrow(
					canvas,
					(cell.startX + (tableCellWidth / 2)) - arrowCircleOffsetRadius,
					(cell.startY + (tableCellHeight / 2)) - arrowCircleOffsetRadius,
					(cell.startX + (tableCellWidth / 2)) - arrowCircleOffsetRadius - (tableCellWidth - 2 * arrowCircleOffsetRadius),
					(cell.startY + (tableCellHeight / 2)) - arrowCircleOffsetRadius - (tableCellWidth - 2 * arrowCircleOffsetRadius),
					arrowPaint
				)
			}
			if (cell.drawArrowTop) {
				drawArrow(
					canvas,
					(cell.startX + (tableCellWidth / 2)),
					(cell.startY + (tableCellHeight / 2)) - arrowCircleOffsetRadius,
					(cell.startX + (tableCellWidth / 2)),
					(cell.startY + (tableCellHeight / 2)) -arrowCircleOffsetRadius - (tableCellHeight - 2 * arrowCircleOffsetRadius),
					arrowPaint
				)
			}
			if (cell.isOnMainPath()) {
				drawArrowDirection(canvas, cell, cell.mainPathDirection)
			}
		}
	}

	private fun drawText(canvas: Canvas, text: String, textX: Float, textY: Float, textPaint: Paint) {
		val textMetrics = textPaint.fontMetrics
		val textHeight = textMetrics.descent - textMetrics.ascent
		val textBaseline = textY - (textHeight / 2) - textMetrics.ascent

		if (textX > left && textX < right && textBaseline > top && textBaseline < bottom) {
			canvas.drawText(text, textX, textBaseline, textPaint)
		}
	}

	private fun drawArrow(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float, paint: Paint) {
		canvas.drawLine(startX, startY, endX, endY, paint)

		val angle = atan2((endY - startY).toDouble(), (endX - startX).toDouble())

		val arrowHeadLength = 20f
		val arrowAngle = Math.toRadians(20.0)

		val x1 = endX - arrowHeadLength * cos(angle - arrowAngle)
		val y1 = endY - arrowHeadLength * sin(angle - arrowAngle)
		val x2 = endX - arrowHeadLength * cos(angle + arrowAngle)
		val y2 = endY - arrowHeadLength * sin(angle + arrowAngle)

		val path = Path()
		path.moveTo(endX, endY)
		path.lineTo(x1.toFloat(), y1.toFloat())
		path.lineTo(x2.toFloat(), y2.toFloat())
		path.lineTo(endX, endY)
		canvas.drawPath(path, paint)
	}

	private fun drawArrowDirection(canvas: Canvas, cell: CellData, direction: PathDirectionEnum) {
		if (direction == PathDirectionEnum.LEFT) {
			drawArrow(
				canvas,
				(cell.startX + (tableCellWidth / 2)) - arrowCircleOffsetRadius,
				cell.startY + (tableCellHeight / 2),
				(cell.startX + (tableCellWidth / 2)) - arrowCircleOffsetRadius - (tableCellWidth - 2 * arrowCircleOffsetRadius),
				cell.startY + (tableCellHeight / 2),
				arrowPaint
			)
		}
		if (direction == PathDirectionEnum.DIAGONAL) {
			drawArrow(
				canvas,
				(cell.startX + (tableCellWidth / 2)) - arrowCircleOffsetRadius,
				(cell.startY + (tableCellHeight / 2)) - arrowCircleOffsetRadius,
				(cell.startX + (tableCellWidth / 2)) - arrowCircleOffsetRadius - (tableCellWidth - 2 * arrowCircleOffsetRadius),
				(cell.startY + (tableCellHeight / 2)) - arrowCircleOffsetRadius - (tableCellWidth - 2 * arrowCircleOffsetRadius),
				arrowPaint
			)
		}
		if (direction == PathDirectionEnum.TOP) {
			drawArrow(
				canvas,
				(cell.startX + (tableCellWidth / 2)),
				(cell.startY + (tableCellHeight / 2)) - arrowCircleOffsetRadius,
				(cell.startX + (tableCellWidth / 2)),
				(cell.startY + (tableCellHeight / 2)) -arrowCircleOffsetRadius - (tableCellHeight - 2 * arrowCircleOffsetRadius),
				arrowPaint
			)
		}
	}

	private fun getRow(y: Float): Int {
		return ((y - (y % tableCellHeight)) / tableCellHeight).roundToInt()
	}

	private fun getColumn(x: Float): Int {
		return ((x - (x % tableCellWidth)) / tableCellWidth).roundToInt()
	}

	private fun getIndexBasedOnPosition(row: Int, column: Int): Int {
		return row * (tableData.tableParameters.columnCount + 2) + column
	}


	/**
	 * Handling all panning and zooming on canvas
	 **/
	private lateinit var scaleGestureDetector: ScaleGestureDetector
	private var scaleFactor = 1f
	private var xOffset = tableCellWidth
	private var yOffset = tableCellHeight

	override fun onScale(detector: ScaleGestureDetector): Boolean {
		scaleFactor *= detector.scaleFactor
		scaleFactor = max(1f, min(scaleFactor, 3.0f))
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


	private var startOffsetX: Float = 0f
	private var startOffsetY: Float = 0f
	private var startTime: Long = 0

	override fun onTouchEvent(e: MotionEvent): Boolean {
		scaleGestureDetector.onTouchEvent(e)

		when (e.action) {
			MotionEvent.ACTION_DOWN -> {
				prevX = e.x
				prevY = e.y
				startOffsetX = xOffset
				startOffsetY = yOffset
				startTime = System.currentTimeMillis()
			}

			MotionEvent.ACTION_MOVE -> {
				if (!scaleGestureDetector.isInProgress) {
					val dx = e.x - prevX
					val dy = e.y - prevY

					xOffset += dx / scaleFactor
					yOffset += dy / scaleFactor

					if (this::tableData.isInitialized) {
						val totalTableWidth = (tableCellWidth * (tableData.tableParameters.columnCount + 3))
						val totalTableHeight = (tableCellHeight * (tableData.tableParameters.rowCount + 2 + (5 / scaleFactor)))

						val visibleWidth = width / scaleFactor
						val visibleHeight = height / scaleFactor

						val maxXOffset = max(0f, totalTableWidth - visibleWidth)
						val maxYOffset = max(0f, totalTableHeight - visibleHeight)

						xOffset = max(-maxXOffset, min(xOffset, tableCellWidth))
						yOffset = max(-maxYOffset, min(yOffset, tableCellHeight))

						invalidate()
					}
				}

				prevX = e.x
				prevY = e.y
			}
			MotionEvent.ACTION_UP -> {
				val dx = e.x - prevX
				val dy = e.y - prevY
				val dOffsetX = startOffsetX - xOffset
				val dOffsetY = startOffsetY - yOffset
				val dt = System.currentTimeMillis() - startTime

				if (dx < 1 && dy < 1 && dOffsetX < 1 && dOffsetY < 1 && dt < 200) {
					val column = getColumn((e.x / scaleFactor) - xOffset)
					val row = getRow((e.y / scaleFactor) - yOffset)

					if (column < tableData.tableParameters.columnCount + 2 && row < tableData.tableParameters.rowCount + 2) {
						if (this::currentActiveCell.isInitialized) {
							val isHighlighted = currentActiveCell.isHighlighted

							currentActiveCell.toggleHighlight(false)
							currentActiveNeighbours.forEach {
								it.toggleNeighbour(false)
							}

							if (!isHighlighted && currentActiveCell.cellData.position == Pair(row, column)) {
								currentActiveCell.toggleHighlight(true)
								currentActiveNeighbours.forEach {
									it.toggleNeighbour(true)
								}
							} else if (currentActiveCell.cellData.position != Pair(row, column)) {
								currentActiveNeighbours.clear()

								currentActiveCell = cells[getIndexBasedOnPosition(row, column)]
								currentActiveCell.toggleHighlight(true)

								currentActiveNeighbours.addAll(currentActiveCell.cellData.valuePredecessors.map {
									val cell = cells[getIndexBasedOnPosition(it.predecessor.first, it.predecessor.second)]
									cell.toggleNeighbour(true)
									cell
								})
							}
						} else {
							currentActiveCell = cells[getIndexBasedOnPosition(row, column)]
							currentActiveCell.toggleHighlight(true)

							currentActiveNeighbours.addAll(currentActiveCell.cellData.valuePredecessors.map {
								val cell = cells[getIndexBasedOnPosition(it.predecessor.first, it.predecessor.second)]
								cell.toggleNeighbour(true)
								cell
							})
						}

						invalidate()
					}

				}
			}
		}
		return true
	}

	companion object {

		@JvmStatic
		@BindingAdapter("android:tableData")
		fun ConnectedTableView.setTableData(tableData: TableItem?) {
			if (tableData == null) return

			this.tableData = tableData
			this.createCells()
			this. createMainPath()

			invalidate()
		}
	}

	private fun createMainPath() {
		var currentPosition: Int = getIndexBasedOnPosition(tableData.scorePosition.first + 1, tableData.scorePosition.second + 1)

		while (currentPosition != tableData.tableParameters.columnCount) {
			val currentCell = cells[currentPosition]
			val nextPosition = currentCell.setMainPathActive(true)

			currentActiveMainPath.add(currentCell)

			if (nextPosition.first != -1) {
				val position = getIndexBasedOnPosition(nextPosition.first, nextPosition.second)
				currentPosition = position
			} else {
				break
			}
		}

		calculateNewMainData()
	}

	fun moveMainPathForward() {
		val firstCellPosition = currentActiveMainPath.size - currentActiveMainPath.reversed().indexOfFirst { it.cellData.valuePredecessors.size > 1 && it.currentPredecessorIndex < it.cellData.valuePredecessors.size - 1}

		if (firstCellPosition == -1 || firstCellPosition > currentActiveMainPath.size - 1) return

		for (i in firstCellPosition until currentActiveMainPath.size) {
			currentActiveMainPath[i].setMainPathActive(false)
		}
		currentActiveMainPath = ArrayList(currentActiveMainPath.dropLast(currentActiveMainPath.size - firstCellPosition))

		val firstCell = currentActiveMainPath[firstCellPosition - 1]
		val startPosition = firstCell.moveMainPathForward()

		var currentPosition = getIndexBasedOnPosition(startPosition.first, startPosition.second)

		while (currentPosition != tableData.tableParameters.columnCount) {
			val currentCell = cells[currentPosition]
			val nextPosition = currentCell.setMainPathActive(true)

			currentActiveMainPath.add(currentCell)

			if (nextPosition.first != -1) {
				val position = getIndexBasedOnPosition(nextPosition.first, nextPosition.second)
				currentPosition = position
			} else {
				break
			}
		}

		calculateNewMainData()
		invalidate()
	}

	fun moveMainPathBack() {
		val firstCellPosition = currentActiveMainPath.size - currentActiveMainPath.reversed().indexOfFirst { it.currentPredecessorIndex > 0 }

		if (firstCellPosition == -1 || firstCellPosition > currentActiveMainPath.size - 1) return

		for (i in firstCellPosition until currentActiveMainPath.size) {
			currentActiveMainPath[i].setMainPathActive(false)
		}
		currentActiveMainPath = ArrayList(currentActiveMainPath.dropLast(currentActiveMainPath.size - firstCellPosition))

		val firstCell = currentActiveMainPath[firstCellPosition - 1]
		val startPosition = firstCell.moveMainPathBackwards()

		var currentPosition = getIndexBasedOnPosition(startPosition.first, startPosition.second)

		while (currentPosition != tableData.tableParameters.columnCount) {
			val currentCell = cells[currentPosition]
			val nextPosition = currentCell.setMainPathActive(true)

			currentActiveMainPath.add(currentCell)

			if (nextPosition.first != -1) {
				val position = getIndexBasedOnPosition(nextPosition.first, nextPosition.second)
				currentPosition = position
			} else {
				break
			}
		}

		calculateNewMainData()
		invalidate()
	}

	private fun calculateNewMainData() {
		var seqA = ""
		var seqB = ""
		var result = ""
		for (cell in currentActiveMainPath.reversed()) {
			if (cell.currentPredecessorIndex >= 0) {
				val predecessor = cell.cellData.valuePredecessors.reversed()[cell.currentPredecessorIndex]
				seqA += predecessor.seqA
				seqB += predecessor.seqB
				result += predecessor.result
			}
		}

		if (!this::currentMainPathData.isInitialized) {
			currentMainPathData = AlignmentResultItem(
				resultLineSequenceA = seqA,
				resultLineSequenceB = seqB,
				resultLIneActionsTaken = result
			)
		} else {
			currentMainPathData.resultLineSequenceA = seqA
			currentMainPathData.resultLineSequenceB = seqB
			currentMainPathData.resultLIneActionsTaken = result
		}
	}

	private fun createCells() {
		val columns = tableData.tableParameters.columnCount + 2
		val rows = tableData.tableParameters.rowCount + 2
		val table = Array(rows * columns) {
			val rowIndex = it / columns
			val columnIndex = it % columns
			val position = Pair(rowIndex, columnIndex)

			val isContentCell = rowIndex != 0 && columnIndex != 0
			val contentIndex = ((rowIndex - 1) * (columns - 1)) + (columnIndex - 1)

			CellData(
				columnIndex * tableCellWidth,
				rowIndex * tableCellHeight,
				if (isContentCell && columnIndex > 0) CellItem(
					value = tableData.tableData[contentIndex].value,
					position = position,
					valuePredecessors = tableData.tableData[contentIndex].valuePredecessors
						.map {item ->
							item.predecessor = Pair(item.predecessor.first + 1, item.predecessor.second + 1)
							item
						} as ArrayList<CellResultItem>
				)
				else CellItem(
					value = if (rowIndex > 1 && columnIndex == 0) {
						tableData.tableParameters.sequenceA[rowIndex - 2].toString()
					} else if (rowIndex == 0 && columnIndex > 1) {
						tableData.tableParameters.sequenceB[columnIndex - 2].toString()
					} else if (rowIndex == 0 && columnIndex == 0) {
						"S"
					} else {
						""
					},
					position = position,
					valuePredecessors = arrayListOf()
				),
				isContentCell
			)
		}

		cells.addAll(table)
	}

	private class CellData(
		val startX: Float,
		val startY: Float,
		val cellData: CellItem,
		val canBeHighlighted: Boolean,
	) {
		var isHighlighted = false
		var isNeighbourHighlighted = false

		var mainPathDirection: PathDirectionEnum = PathDirectionEnum.NONE
		var currentPredecessorIndex: Int = -1

		var drawArrowLeft = false
		var drawArrowDiagonal = false
		var drawArrowTop = false

		fun toggleHighlight(highlighted: Boolean) {
			if (canBeHighlighted) {
				isHighlighted = highlighted
				if (isHighlighted) {
					for (predecessor in cellData.valuePredecessors) {
						if (predecessor.predecessor.first == cellData.position.first && predecessor.predecessor.second < cellData.position.second) {
							drawArrowLeft = true
						} else if (predecessor.predecessor.first < cellData.position.first && predecessor.predecessor.second < cellData.position.second) {
							drawArrowDiagonal = true
						} else if (predecessor.predecessor.first < cellData.position.first && predecessor.predecessor.second == cellData.position.second) {
							drawArrowTop = true
						}
					}
				} else {
					drawArrowLeft = false
					drawArrowDiagonal = false
					drawArrowTop = false
				}
			}
		}

		fun toggleNeighbour(toggle: Boolean) {
			isNeighbourHighlighted = toggle
		}

		fun setMainPathActive(toggle: Boolean): Pair<Int, Int> {
			if (toggle) {
				return if (cellData.valuePredecessors.size > 0) {
					currentPredecessorIndex = 0
					val predecessor = cellData.valuePredecessors.reversed()[currentPredecessorIndex]
					if (predecessor.predecessor.first == cellData.position.first && predecessor.predecessor.second < cellData.position.second) {
						mainPathDirection = PathDirectionEnum.LEFT
					} else if (predecessor.predecessor.first < cellData.position.first && predecessor.predecessor.second < cellData.position.second) {
						mainPathDirection = PathDirectionEnum.DIAGONAL
					} else if (predecessor.predecessor.first < cellData.position.first && predecessor.predecessor.second == cellData.position.second) {
						mainPathDirection = PathDirectionEnum.TOP
					}

					predecessor.predecessor
				} else {
					mainPathDirection = PathDirectionEnum.ROOT
					Pair(-1, -1)
				}

			} else {
				mainPathDirection = PathDirectionEnum.NONE
				currentPredecessorIndex = -1

				return Pair(-1, -1)
			}
		}

		fun moveMainPathForward(): Pair<Int, Int> {
			return if (cellData.valuePredecessors.size == currentPredecessorIndex + 1) {
				Pair(-1, -1)
			} else {
				currentPredecessorIndex++
				val predecessor = cellData.valuePredecessors.reversed()[currentPredecessorIndex]
				if (predecessor.predecessor.first == cellData.position.first && predecessor.predecessor.second < cellData.position.second) {
					mainPathDirection = PathDirectionEnum.LEFT
				} else if (predecessor.predecessor.first < cellData.position.first && predecessor.predecessor.second < cellData.position.second) {
					mainPathDirection = PathDirectionEnum.DIAGONAL
				} else if (predecessor.predecessor.first < cellData.position.first && predecessor.predecessor.second == cellData.position.second) {
					mainPathDirection = PathDirectionEnum.TOP
				}
				predecessor.predecessor
			}
		}

		fun moveMainPathBackwards(): Pair<Int, Int> {
			return if (currentPredecessorIndex - 1 < 0) {
				Pair(-1, -1)
			} else {
				currentPredecessorIndex--
				val predecessor = cellData.valuePredecessors.reversed()[currentPredecessorIndex]
				if (predecessor.predecessor.first == cellData.position.first && predecessor.predecessor.second < cellData.position.second) {
					mainPathDirection = PathDirectionEnum.LEFT
				} else if (predecessor.predecessor.first < cellData.position.first && predecessor.predecessor.second < cellData.position.second) {
					mainPathDirection = PathDirectionEnum.DIAGONAL
				} else if (predecessor.predecessor.first < cellData.position.first && predecessor.predecessor.second == cellData.position.second) {
					mainPathDirection = PathDirectionEnum.TOP
				}
				predecessor.predecessor
			}
		}

		fun isOnMainPath() = mainPathDirection != PathDirectionEnum.NONE

	}

	private enum class PathDirectionEnum {
		NONE,
		ROOT,
		LEFT,
		DIAGONAL,
		TOP
	}
}