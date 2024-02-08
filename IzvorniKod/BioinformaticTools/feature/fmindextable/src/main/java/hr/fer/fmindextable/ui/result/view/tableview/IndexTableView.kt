package hr.fer.fmindextable.ui.result.view.tableview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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
import hr.fer.common.R
import hr.fer.common.utils.MultiPropertyAnimator
import hr.fer.fmindextable.model.FMIndexTableData
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin


class IndexTableView : View, ScaleGestureDetector.OnScaleGestureListener {

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
	private val tableCellWidth: Float = 100f
	private val tableCellHeight: Float = 100f
	private val tableLineThickness: Float = 3f
	private val textFontSize = 30f
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
	private val tableCellHighlightedPaint = Paint().apply {
		color = Color.rgb(200,255,200)
		style = Paint.Style.FILL
	}
	private val tableCellHighlightedRedPaint = Paint().apply {
		color = Color.rgb(255,200,200)
		style = Paint.Style.FILL
	}
	private val textPaint = Paint().apply {
		color = Color.BLACK
		textSize = textFontSize
		typeface = ResourcesCompat.getFont(context, R.font.fira_code_regular)?.let { Typeface.create(it, Typeface.NORMAL) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}
	private val textPaintSmall = Paint().apply {
		color = Color.BLACK
		textSize = textFontSize * 0.75f
		typeface = ResourcesCompat.getFont(context, R.font.fira_code_regular)?.let { Typeface.create(it, Typeface.NORMAL) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}

	private lateinit var tableData: FMIndexTableData
	private lateinit var currentStep: IndexStep
	private var currentStepPosition: Int = -1
	private var listOfSteps: ArrayList<IndexStep> = arrayListOf()

	private val arrowPaint = Paint().apply {
		color = Color.BLACK
		strokeWidth = 4f
		style = Paint.Style.FILL_AND_STROKE
		isAntiAlias = true
	}

	private val currentResultPaint = Paint().apply {
		color = Color.RED
		strokeWidth = tableLineThickness * 4
		alpha = 255
		style = Paint.Style.STROKE
	}

	private val resultPaint = Paint().apply {
		color = Color.RED
		strokeWidth = tableLineThickness
		alpha = 255
		style = Paint.Style.STROKE
	}


	private val autoPlayHandler: Handler = Handler(Looper.getMainLooper())
	private val autoPlayStepPauseDuration = 1200L


	private fun init() {
		scaleGestureDetector = ScaleGestureDetector(context, this)
		scaleFactor = 1.5f
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
		drawInitialTable(canvas)
		drawStep(canvas)
		drawCurrentStateOfPatternAndSequence(canvas)

		canvas.restore()
	}

	private fun drawInitialTable(canvas: Canvas) {
		if (!this::tableData.isInitialized) return

		val columns = 3 + tableData.charRankings.size - 1
		val rows = tableData.sequence.length + 2

		val characters: ArrayList<String> = tableData.charRankings.keys
			.map { it.toString() }
			.filter { it != "$" }
			.sorted()
			.toList() as ArrayList<String>

		for (i in 0 until columns) {
			for (j in 0 until rows) {
				val startX = i * tableCellWidth
				val startY = j * tableCellHeight

				var shouldHighlight = false
				var shouldHighlightRed = false
				if (this::currentStep.isInitialized && currentStep.isHighlighted) {
					if (currentStep.highlightedCells.firstOrNull { it.first == i && it.second == j } != null) {
						shouldHighlight = true

						if (currentStep.shouldDifferentiateBoxes && currentStep.patternResultPositions.firstOrNull { it.first == i + 1 && it.second == j } == null) {
							shouldHighlightRed = true
						}
					}
				}

				canvas.drawRect(
					startX,
					startY,
					startX + tableCellWidth,
					startY + tableCellHeight,
					if (shouldHighlightRed) {
						tableCellHighlightedRedPaint
					} else if (shouldHighlight) {
						tableCellHighlightedPaint
					} else if (j > 1) {
						tableCellPaint
					} else {
						tableCellEdgePaint
					}
				)

				if (j > 1) {
					val textX = startX + (tableCellWidth / 2)
					val textY = startY + (tableCellHeight / 2)

					when (i) {
						0 -> {
							drawText(canvas, tableData.suffixArray[j - 2].toString(), textX, textY, textPaint)
						}
						1 -> {
							drawText(canvas, tableData.firstString[j - 2].first, textX, textY, textPaint)
							val textWidth = textPaint.measureText(tableData.firstString[j - 2].first)

							val textSmallX = textX + textWidth
							val textSmallY = textY + ((textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent) / 4)
							drawText(canvas, tableData.firstString[j - 2].second.toString(), textSmallX, textSmallY, textPaintSmall)
						}
						2 -> {
							drawText(canvas, tableData.transformedStringBWT[j - 2].toString(), textX, textY, textPaint)
						}
						else -> {
							val key: Char = characters[i - 3].toCharArray().first()
							drawText(canvas, tableData.charRankings[key]!![j - 2].toString(), textX, textY, textPaint)
						}
					}
				}
			}
		}

		for (i in 0..columns) {
			val startX = i * tableCellWidth
			val startY = if (i == 0 || i == columns || i == 3) 0f else tableCellHeight
			val endY = rows * tableCellHeight

			if (startX > left && startX < right) {
				canvas.drawLine(startX, startY, startX, endY, tablePaint)
			}

			var textX = startX + (tableCellWidth / 2)
			var textY = startY + (tableCellHeight / 2)

			when (i) {
				0 -> {
					textY += tableCellHeight
					drawText(canvas, "SA", textX, textY, textPaint)
				}
				1 -> {
					drawText(canvas, "F", textX, textY, textPaint)
				}
				2 -> {
					drawText(canvas, "B", textX, textY, textPaint)
				}
				else -> {
					if (i < columns) {
						if (i == 3) {
							textY += tableCellHeight
						}
						drawText(canvas, characters[i - 3], textX, textY, textPaint)

						if (i == 3) {
							textX -= (tableCellWidth / 2)
							textX += (((tableData.charRankings.size - 1) * tableCellWidth) / 2)
							textY -= tableCellHeight

							drawText(canvas, "Character Rank", textX, textY, textPaint)
						}
					}
				}
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

	private fun drawCurrentStateOfPatternAndSequence(canvas: Canvas) {
		if (!this::tableData.isInitialized) return

		val textCenterX = (tableCellWidth / scaleFactor) - xOffset
		val textY = (tableCellHeight) - yOffset

		val textWidth = textPaint.measureText("Sequence: ${tableData.sequence}")
		val textPatternWidth = textPaint.measureText("Pattern: ${tableData.pattern}")

		val marginHorizontal = 20f
		val marginVertical = 10f

		val rectLeft = textCenterX - marginHorizontal
		val rectTop = textY - marginVertical - ((textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent) / 2)
		val rectRight = textCenterX + max(textWidth, textPatternWidth) + marginHorizontal
		val rectBottom = textY + (tableCellHeight / 2) + marginVertical

		canvas.drawRoundRect(rectLeft, rectTop, rectRight, rectBottom, 8f, 8f, tableCellPaint)

		canvas.drawText("Sequence: ${tableData.sequence}", textCenterX + (textWidth / 2), textY, textPaint)
		canvas.drawText("Pattern: ${tableData.pattern}", textCenterX + (textPatternWidth / 2), textY + (tableCellHeight / 2), textPaint)

		if (this::currentStep.isInitialized ) {
			val textWidthStart = textPaint.measureText("Pattern: ")
			val textWidthLetter = textPaint.measureText("A")

			val startX = textCenterX + textWidthStart + (textWidthLetter * (tableData.pattern.length - currentStep.patternPosition - 1))
			val startY = textY + (tableCellHeight / 2) - ((textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent) * 0.75f)
			canvas.drawRect(startX, startY, startX + (textWidthLetter * (currentStep.patternPosition + 1)), startY + (textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent), resultPaint)
		}

		if (this::currentStep.isInitialized && currentStep.isFinalStep && currentStep.finalResultDrawn) {
			val textWidthStart = textPaint.measureText("Sequence: ")
			val textWidthLetter = textPaint.measureText("A")
			val textWidthPattern = textPaint.measureText(tableData.pattern)

			currentStep.finalStepPositions.forEach {
				val startX = textCenterX + textWidthStart + (textWidthLetter * it)
				val startY = textY - ((textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent) * 0.75f)
				canvas.drawRect(startX, startY, startX + textWidthPattern, startY + (textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent), resultPaint)
			}
		}
	}

	private fun drawStep(canvas: Canvas) {
		if (!this::currentStep.isInitialized) return
		if (currentStep.patternResultBoxesDrawn) {
			currentStep.patternResultPositions.forEach { position ->
				val startX = position.first * tableCellWidth
				val startY = position.second * tableCellHeight
				canvas.drawRect(startX, startY, startX + tableCellWidth, startY + tableCellHeight, currentResultPaint)
			}
		}
		if (currentStep.arrowsDrawn) {
			currentStep.patternResultPositions.forEachIndexed { index, position ->
				val startX = position.first * tableCellWidth + (tableCellWidth / 2) + 15
				val startY = position.second * tableCellHeight + (tableCellHeight / 2)
				val endX = currentStep.rankResultingBoxPositions[index].first * tableCellWidth + (tableCellWidth / 2) - 15
				val endY = startY

				drawArrow(canvas, startX, startY, endX, endY, arrowPaint)
			}
		}
		if (currentStep.rankResultBoxesDrawn) {
			currentStep.rankResultingBoxPositions.forEach { position ->
				val startX = position.first * tableCellWidth
				val startY = position.second * tableCellHeight
				canvas.drawRect(startX, startY, startX + tableCellWidth, startY + tableCellHeight, currentResultPaint)
			}
		}
		if (currentStep.arrowsToNextDrawn) {
			currentStep.rankResultingBoxPositions.forEachIndexed { index, position ->
				val startX = position.first * tableCellWidth + (tableCellWidth / 2) - 15
				val startY = position.second * tableCellHeight + (tableCellHeight / 2)
				val endX = currentStep.nextElementPositions[index].first * tableCellWidth + (tableCellWidth / 2) + 30
				var endY = currentStep.nextElementPositions[index].second * tableCellWidth + (tableCellHeight / 2)
				if (endY < startY) {
					endY += 20
				}

				drawArrow(canvas, startX, startY, endX, endY, arrowPaint)
			}
		}
		if (currentStep.nextElementBoxesDrawn) {
			currentStep.nextElementPositions.forEach { position ->
				val startX = position.first * tableCellWidth
				val startY = position.second * tableCellHeight
				canvas.drawRect(startX, startY, startX + tableCellWidth, startY + tableCellHeight, currentResultPaint)
			}
		}
		if (currentStep.finalResultDrawn) {
			currentStep.resultingElementsSAPositions.forEach { position ->
				val startX = position.first * tableCellWidth
				val startY = position.second * tableCellHeight
				canvas.drawRect(startX, startY, startX + tableCellWidth, startY + tableCellHeight, currentResultPaint)
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



	/**
	 * User animation control functions
	 */
	fun nextStep() {
//		autoPlayHandler.removeCallbacks(animateRunnable)

		if (!this::currentStep.isInitialized) {
			currentStepPosition++
			currentStep = listOfSteps[currentStepPosition]
		}
		val progress = currentStep.nextStep()
		if (progress && !currentStep.hasNotFoundResults) {
			currentStepPosition++
			if (currentStepPosition > listOfSteps.size) currentStepPosition = listOfSteps.size
			if (currentStepPosition < listOfSteps.size) {
				currentStep = listOfSteps[currentStepPosition]
				currentStep.nextStep()
			}
		}

		invalidate()
	}
	fun previousStep() {
//		autoPlayHandler.removeCallbacks(animateRunnable)
		if (!this::currentStep.isInitialized) return

		val progress = currentStep.previousStep()
		if (progress) {
			currentStepPosition--
			if (currentStepPosition < -1) currentStepPosition = -1
			if (currentStepPosition > -1) {
				currentStep = listOfSteps[currentStepPosition]
			}
		}

		invalidate()
	}

	/**
	 * Handling all panning and zooming on canvas
	 **/
	private lateinit var scaleGestureDetector: ScaleGestureDetector
	private var scaleFactor = 1.5f
	private var xOffset = tableCellWidth
	private var yOffset = tableCellHeight * 2

	override fun onScale(detector: ScaleGestureDetector): Boolean {
		scaleFactor *= detector.scaleFactor
		scaleFactor = max(1f, min(scaleFactor, 1.8f))
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

					xOffset += dx / scaleFactor
					yOffset += dy / scaleFactor

					if (this::tableData.isInitialized) {
						val totalTableWidth = ((tableCellWidth * (tableData.charRankings.size + 2)))
						val totalTableHeight = (tableCellHeight * (tableData.sequence.length + 3 + (5 / scaleFactor)))

						val visibleWidth = width / scaleFactor
						val visibleHeight = height / scaleFactor

						val maxXOffset = max(0f, totalTableWidth - visibleWidth)
						val maxYOffset = max(0f, totalTableHeight - visibleHeight)

						xOffset = max(-maxXOffset, min(xOffset, tableCellWidth))
						yOffset = max(-maxYOffset, min(yOffset, tableCellHeight * 2))

						invalidate()
					}
				}

				prevX = e.x
				prevY = e.y
			}
		}
		return true
	}

	private fun calculateAllSteps() {
		var currentPatternPosition = 0
		var lastResultPositions: ArrayList<Pair<Int, Int>> = arrayListOf()

		val characters: ArrayList<String> = tableData.charRankings.keys
			.map { it.toString() }
			.filter { it != "$" }
			.sorted()
			.toList() as ArrayList<String>

		while (currentPatternPosition < tableData.pattern.length) {
			if (currentPatternPosition == tableData.pattern.length - 1) {

				val finishedResults: ArrayList<Pair<Int, Int>> = arrayListOf()
				lastResultPositions.forEach {
					finishedResults.add(Pair<Int, Int>(0, it.second))
				}

				val results: ArrayList<Int> = arrayListOf()
				finishedResults.forEach {
					results.add(
						tableData.suffixArray[it.second - 2]
					)
				}

				listOfSteps.add(
					IndexStep(
						highlightedCells = lastResultPositions,
						patternResultPositions = arrayListOf(),
						rankResultingBoxPositions = arrayListOf(),
						nextElementPositions = arrayListOf(),
						resultingElementsSAPositions = finishedResults,
						isFinalStep = true,
						finalStepPositions = results,
						patternPosition = currentPatternPosition,
						currentPatternCharacter = "",
						nextPatternCharacter = "",
					)
				)

				break
			}

			val patternChar = tableData.pattern.reversed()[currentPatternPosition]
			val highlightedPositions: ArrayList<Pair<Int, Int>> = arrayListOf()

			if (listOfSteps.isEmpty()) {
				tableData.firstString.forEachIndexed { index, c ->
					if (c.first == patternChar.toString()) {
						highlightedPositions.add(
							Pair<Int, Int>(1, index + 2)
						)
					}
				}
			} else {
				highlightedPositions.addAll(lastResultPositions)
			}

			val patternResultPositions: ArrayList<Pair<Int, Int>> = arrayListOf()
			val nextPatternIndexes: ArrayList<Int> = arrayListOf()
			val nextPatternChar = tableData.pattern.reversed()[currentPatternPosition + 1]

			tableData.transformedStringBWT.forEachIndexed { index, c ->
				if (c == nextPatternChar && highlightedPositions.firstOrNull { it.second == index + 2 } != null) {
					patternResultPositions.add(
						Pair<Int, Int>(2, index + 2)
					)
					nextPatternIndexes.add(index)
				}
			}

			val rankResultPosition: ArrayList<Pair<Int, Int>> = arrayListOf()
			val rankPositions: ArrayList<Int> = arrayListOf()

			if (patternResultPositions.isEmpty()) {

				listOfSteps.add(
					IndexStep(
						highlightedCells = highlightedPositions,
						patternResultPositions = patternResultPositions,
						rankResultingBoxPositions = rankResultPosition,
						nextElementPositions = arrayListOf(),
						resultingElementsSAPositions = arrayListOf(),
						hasNotFoundResults = true,
						patternPosition = currentPatternPosition,
						currentPatternCharacter = patternChar.toString(),
						nextPatternCharacter = nextPatternChar.toString(),
					)
				)

			} else {
				patternResultPositions.forEachIndexed { index, pair ->
					rankResultPosition.add(
						Pair<Int, Int>(pair.first + characters.indexOf(nextPatternChar.toString()) + 1, pair.second)
					)
					rankPositions.add(tableData.charRankings[nextPatternChar]!![nextPatternIndexes[index]])
				}
			}

			val nextElementPositions: ArrayList<Pair<Int, Int>> = arrayListOf()

			rankPositions.forEach {
				nextElementPositions.add(
					Pair<Int, Int>(
						1,
						tableData.firstString.indexOfFirst { item -> item.first == nextPatternChar.toString() && item.second == it } + 2
					)
				)
			}

			lastResultPositions = nextElementPositions

			listOfSteps.add(
				IndexStep(
					highlightedCells = highlightedPositions,
					patternResultPositions = patternResultPositions,
					rankResultingBoxPositions = rankResultPosition,
					nextElementPositions = nextElementPositions,
					resultingElementsSAPositions = arrayListOf(),
					currentPatternCharacter = patternChar.toString(),
					nextPatternCharacter = nextPatternChar.toString(),
					patternPosition = currentPatternPosition
				)
			)

			currentPatternPosition++
		}
	}

	companion object {

		@JvmStatic
		@BindingAdapter("android:tableData")
		fun IndexTableView.setTableData(tableData: FMIndexTableData?) {
			if (tableData == null) return

			this.tableData = tableData
			calculateAllSteps()

			invalidate()
		}
	}

	fun getText(): String {
		return currentStep.currentStepDescription
	}

	private class IndexStep(
		val highlightedCells: ArrayList<Pair<Int, Int>>, //this is equal to the nextElementPosition from previous step
		val patternResultPositions: ArrayList<Pair<Int, Int>>,
		val rankResultingBoxPositions: ArrayList<Pair<Int, Int>>,
		val nextElementPositions: ArrayList<Pair<Int, Int>>,
		val resultingElementsSAPositions: ArrayList<Pair<Int, Int>>,
		val patternPosition: Int,
		val currentPatternCharacter: String,
		val nextPatternCharacter: String,
		val finalStepPositions: ArrayList<Int> = arrayListOf(),
		val isFinalStep: Boolean = false,
		val hasNotFoundResults: Boolean = false
	) {

		var isHighlighted: Boolean = false
		var patternResultBoxesDrawn: Boolean = false
		var arrowsDrawn: Boolean = false
		var rankResultBoxesDrawn: Boolean = false
		var arrowsToNextDrawn: Boolean = false
		var nextElementBoxesDrawn: Boolean = false
		var finalResultDrawn: Boolean = false
		var hasNotFound: Boolean = false
		var shouldDifferentiateBoxes = false

		var currentStepDescription: String = ""

		fun nextStep(): Boolean {
			if (!isHighlighted) {
				isHighlighted = true
				currentStepDescription = "Highlight the current pattern character ($currentPatternCharacter) in the F column."
				return false
			}
			shouldDifferentiateBoxes = true
			if (isFinalStep) {
				if (!finalResultDrawn) {
					finalResultDrawn = true
					currentStepDescription = "Total results found: ${finalStepPositions.size}\nOn sequence positions: ${finalStepPositions.sorted().joinToString(separator = ", ")}"
					return false
				}
				return true
			}

			if (hasNotFoundResults) {
				if (!hasNotFound) {
					hasNotFound = true
					currentStepDescription = "No pattern match found."
					return false
				}
			}

			if (hasNotFound) {
				return false
			}

			if (!patternResultBoxesDrawn && !arrowsToNextDrawn) {
				currentStepDescription = "Find the next pattern character ($nextPatternCharacter) in the B column."
				patternResultBoxesDrawn = true
				return false
			}
			if (!arrowsDrawn && !arrowsToNextDrawn) {
				currentStepDescription = "Look up the ranks for the found character in the characters rank column."
				arrowsDrawn = true
				return false
			}
			if (!rankResultBoxesDrawn) {
				rankResultBoxesDrawn = true
				currentStepDescription = "Mark the ranks of the character."
				return false
			}
			if (!arrowsToNextDrawn) {
				currentStepDescription = "Look up the position of the ranks in the F column."
				patternResultBoxesDrawn = false
				arrowsDrawn = false
				arrowsToNextDrawn = true
				return false
			}
			if (!nextElementBoxesDrawn) {
				currentStepDescription = "Mark the next pattern character ($nextPatternCharacter) for the next step."
				nextElementBoxesDrawn = true
				return false
			}

			return true
		}

		fun previousStep(): Boolean {
			if (nextElementBoxesDrawn) {
				nextElementBoxesDrawn = false
				currentStepDescription = "Look up the position of the ranks in the F column."
				return false
			}
			if (arrowsToNextDrawn) {
				arrowsToNextDrawn = false
				patternResultBoxesDrawn = true
				arrowsDrawn = true
				currentStepDescription = "Mark the ranks of the character."
				return false
			}
			if (rankResultBoxesDrawn) {
				currentStepDescription = "Look up the ranks for the found character in the characters rank column."
				rankResultBoxesDrawn = false
				return false
			}
			if (arrowsDrawn) {
				currentStepDescription = "Find the next pattern character ($nextPatternCharacter) in the B column."
				arrowsDrawn = false
				return false
			}
			shouldDifferentiateBoxes = false
			if (patternResultBoxesDrawn) {
				patternResultBoxesDrawn = false
				currentStepDescription = "Highlight the current pattern character ($currentPatternCharacter) in the F column."
				return false
			}
			if (hasNotFoundResults) {
				if (hasNotFound) {
					hasNotFound = false
					currentStepDescription = "Highlight the current pattern character ($currentPatternCharacter) in the F column."
					return false
				}
			}
			if (isFinalStep) {
				if (finalResultDrawn) {
					finalResultDrawn = false
					currentStepDescription = "Highlight the current pattern character ($currentPatternCharacter) in the F column."
					return false
				}
			}
			if (isHighlighted) {
				isHighlighted = false
				currentStepDescription = ""
				return true
			}

			return true
		}


	}
}