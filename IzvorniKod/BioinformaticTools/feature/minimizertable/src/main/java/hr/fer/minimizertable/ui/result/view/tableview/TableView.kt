package hr.fer.minimizertable.ui.result.view.tableview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import hr.fer.common.utils.MultiPropertyAnimator
import hr.fer.minimizertable.model.MinimizerStepData
import hr.fer.minimizertable.model.MinimizersTableData
import kotlin.math.max
import kotlin.math.min
import hr.fer.common.R as commonR


class TableView : View, ScaleGestureDetector.OnScaleGestureListener {

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
	private val tableCellWidthFirst: Float = 300f
	private val tableCellHeight: Float = 100f
	private val tableLineThickness: Float = 3f
	private val tablePaint = Paint().apply {
		color = Color.LTGRAY
		strokeWidth = tableLineThickness
	}
	private val tablePaintThick = Paint().apply {
		color = Color.BLACK
		strokeWidth = tableLineThickness * 3
	}

	private val textFontSize = 40f
	private val textPaint = Paint().apply {
		color = Color.BLACK
		textSize = textFontSize
		typeface = ResourcesCompat.getFont(context, commonR.font.fira_code_regular)?.let { Typeface.create(it, Typeface.NORMAL) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}
	private val textPaintResult = Paint().apply {
		color = Color.RED
		textSize = textFontSize
		typeface = ResourcesCompat.getFont(context, commonR.font.fira_code_bold)?.let { Typeface.create(it, Typeface.BOLD) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}
	private var textLeft: String = ""
	private var textRight: String = ""
	private val textPaintRed = Paint().apply {
		color = Color.RED
		textSize = textFontSize * 2
		typeface = ResourcesCompat.getFont(context, commonR.font.fira_code_bold)?.let { Typeface.create(it, Typeface.BOLD) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}
	private val textPaintGreen = Paint().apply {
		color = Color.rgb(0, 200, 0)
		textSize = textFontSize * 2
		typeface = ResourcesCompat.getFont(context, commonR.font.fira_code_bold)?.let { Typeface.create(it, Typeface.BOLD) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}
	private val textPaintBlack = Paint().apply {
		color = Color.DKGRAY
		textSize = textFontSize * 2
		typeface = ResourcesCompat.getFont(context, commonR.font.fira_code_bold)?.let { Typeface.create(it, Typeface.BOLD) }
		textAlign = Paint.Align.CENTER
		isAntiAlias = true
	}

	val textPaintCopy = copyPaint(textPaint)
	val textPaintBlackCopy = copyPaint(textPaintBlack)
	val textPaintRedCopy = copyPaint(textPaintRed)
	val textPaintGreenCopy = copyPaint(textPaintGreen)

	private lateinit var tableData: MinimizersTableData
	private lateinit var currentStep: MinimizerStepData
	private var currentStepPosition: Int = -1
	private var completedRowsData: ArrayList<MinimizerStepData> = arrayListOf()
	private var minimizersList = mutableMapOf<Int, String>() //map of minimizers with position, text (example: 1 -> "(ACT, 1)")
	private var currentCompletedWindowRow = 1


	private var currentPositionResultX: Float = 1f
	private var currentPositionResultY: Float = 2f
	private var currentPositionCompareX: Float = 2f
	private var currentPositionCompareY: Float = 2f

	private var currentResultAlpha: Int = 200
	private var currentCompareAlpha: Int = 200
	private val currentResultPaint = Paint().apply {
		color = Color.RED
		strokeWidth = tableLineThickness * 4
		alpha = currentResultAlpha
		style = Paint.Style.STROKE
	}
	private val currentComparePaint = Paint().apply {
		color = Color.rgb(0, 220, 0)
		strokeWidth = tableLineThickness * 4
		alpha = currentCompareAlpha
		style = Paint.Style.STROKE
	}

	private var isAnimating: Boolean = false
	private var isAutoPlay: Boolean = false
	private var shouldAnimateBox: Boolean = true

	private val valueAnimator = MultiPropertyAnimator(
		300L,
		updateListener = {
			currentPositionResultX = it["currentPositionResultX"] as? Float ?: currentPositionResultX
			currentPositionResultY = it["currentPositionResultY"] as? Float ?: currentPositionResultX
			currentPositionCompareX = it["currentPositionCompareX"] as? Float ?: currentPositionCompareX
			currentPositionCompareY = it["currentPositionCompareY"] as? Float ?: currentPositionCompareX
			currentResultAlpha = it["currentResultAlpha"] as? Int ?: currentResultAlpha
			currentCompareAlpha = it["currentCompareAlpha"] as? Int ?: currentCompareAlpha
			xOffset = it["xOffset"] as? Float ?: xOffset
			yOffset = it["yOffset"] as? Float ?: yOffset

			invalidate()
		},
		endListener = {
			//nothing
		}
	)


	private val autoPlayHandler: Handler = Handler(Looper.getMainLooper())
	private val autoPlayStepPauseDuration = 1200L



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
		drawInitialTable(canvas)
		drawAnimatedRowNextStep(canvas)
		drawAllFinishedRows(canvas)

		drawResultingText(canvas)

		canvas.restore()
	}

	private fun drawInitialTable(canvas: Canvas) {
		if (!this::tableData.isInitialized) return

		val columns = tableData.sequence.length + 1
		val rows = tableData.maxWindowRow + 2

		for (i in 0..columns) {
			val startX = if (i == 0) 0f else tableCellWidthFirst + ((i - 1) * tableCellWidth)
			val startY = 0f
			val endY = rows * tableCellHeight

			if (startX > left && startX < right) {
				canvas.drawLine(startX, startY, startX, endY, if (i == 1) tablePaintThick else tablePaint)
			}

			val textX = startX + (if (i == 0) tableCellWidthFirst / 2 else  tableCellWidth / 2)
			val textY = startY + (tableCellHeight / 2)
			val textY2 = startY + tableCellHeight + (tableCellHeight / 2)

			if (i == 0) {
				drawText(canvas, "Position:", textX, textY, textPaint)
				drawText(canvas, "Sequence:", textX, textY2, textPaint)
			} else if (i < columns) {
				drawText(canvas, "$i", textX, textY, textPaint)
				drawText(canvas, "${tableData.sequence[i - 1]}", textX, textY2, textPaint)
			}

		}
		for (i in 0 .. rows) {
			val startX = if (i == 0 || i == 2 || i == rows) 0f else tableCellWidthFirst
			val startY = i * tableCellHeight
			val endX = tableCellWidthFirst + ((columns - 1) * tableCellWidth)

			if (startY > top && startY < bottom) {
				canvas.drawLine(startX, startY, endX, startY,  if (i == 2) tablePaintThick else tablePaint)
			}
		}
	}

	private fun drawAllFinishedRows(canvas: Canvas) {
		if (!this::tableData.isInitialized) return

		for (i in 2..currentCompletedWindowRow) {
			val step = completedRowsData[i - 2]
			drawRow(canvas, i, step, true)
		}
	}

	private fun drawRow(canvas: Canvas, row: Int, step: MinimizerStepData, isCompleted: Boolean) {

		val windowStartX = tableCellWidthFirst + (step.currentWindowStartPos * tableCellWidth) + (tableCellWidth / 2)
		val windowY = row * tableCellHeight + (tableCellHeight / 2)

		if (isCompleted && minimizersList.containsKey(row)) {
			val minimizerX = tableCellWidthFirst / 2
			drawText(canvas, minimizersList[row]!!, minimizerX, windowY, textPaint)
		}

		drawTextForWindow(canvas, step, windowStartX, windowY, isCompleted)
	}

	private fun drawTextForWindow(canvas: Canvas, step: MinimizerStepData, windowStartX: Float, windowY: Float, isCompleted: Boolean) {
		val windowStartPos = step.currentWindowStartPos
		val resultPos = step.windowResultStartPos

		val windowText = step.currentWindow

		for (j in windowText.indices) {
			drawText(
				canvas,
				windowText[j].toString(),
				windowStartX + (j * tableCellWidth),
				windowY,
				if (isCompleted && windowStartPos + j >= resultPos && windowStartPos + j < resultPos + tableData.k) {
					textPaintResult
				} else {
					textPaint
				}
			)
		}
	}

	private fun drawAnimatedRowNextStep(canvas: Canvas) {
		if (!this::tableData.isInitialized) return
		if (currentStepPosition < 0 || currentStepPosition >= tableData.steps.size) {
			if (currentStepPosition == tableData.steps.size && currentCompletedWindowRow <= tableData.maxWindowRow) {
				currentCompletedWindowRow++
				val previousStep = tableData.steps[currentStepPosition - 1]
				val result = "(${previousStep.windowResultStartPos}, ${tableData.sequence.substring(previousStep.windowResultStartPos, previousStep.windowResultStartPos + tableData.k)})"
				if (!minimizersList.values.contains(result)) {
					minimizersList[currentCompletedWindowRow] = result
				}
			}
			return
		}

		drawRow(canvas, currentStep.currentWindowRow + 1, currentStep, false)

		canvas.drawRect(currentPositionResultX, currentPositionResultY, currentPositionResultX + tableCellWidth * tableData.k, currentPositionResultY + tableCellHeight, currentResultPaint)
		if (!shouldAnimateBox) return
		canvas.drawRect(currentPositionCompareX, currentPositionCompareY, (currentPositionCompareX + tableCellWidth * tableData.k) + 20, (currentPositionCompareY + tableCellHeight) + 20, currentComparePaint)
	}

	private fun drawText(canvas: Canvas, text: String, textX: Float, textY: Float, textPaint: Paint) {
		val textMetrics = textPaint.fontMetrics
		val textHeight = textMetrics.descent - textMetrics.ascent
		val textBaseline = textY - (textHeight / 2) - textMetrics.ascent

		if (textX > left && textX < right && textBaseline > top && textBaseline < bottom) {
			canvas.drawText(text, textX, textBaseline, textPaint)
		}
	}

	private fun drawResultingText(canvas: Canvas) {
		if (!this::tableData.isInitialized) return
		if (!shouldAnimateBox) return
		if (textLeft.isEmpty() || textRight.isEmpty()) return
		val textCenterX = ((width / 2) / scaleFactor) - xOffset
		val textY = ((height - 400f) / scaleFactor) - yOffset

		val textWidth = textPaintBlack.measureText("A".repeat(tableData.k))
		val textWidthSpace = textPaintBlack.measureText(" ")

		textPaintCopy.textSize = textPaint.textSize / scaleFactor
		textPaintBlackCopy.textSize = textPaintBlack.textSize / scaleFactor
		textPaintRedCopy.textSize = textPaintRed.textSize / scaleFactor
		textPaintGreenCopy.textSize = textPaintGreen.textSize / scaleFactor

		canvas.drawText("Checking:", textCenterX, textY - (100 / scaleFactor), textPaintCopy)
		canvas.drawText("<", textCenterX, textY, textPaintBlackCopy)
		canvas.drawText(textLeft, textCenterX - ((textWidth / 2) / scaleFactor) - (textWidthSpace / scaleFactor), textY, textPaintRedCopy)
		canvas.drawText(textRight, textCenterX + ((textWidth / 2) / scaleFactor) + (textWidthSpace / scaleFactor), textY, textPaintGreenCopy)
	}

	private fun calculateNexStepPositions() {
		if (currentStepPosition < 0 || currentStepPosition >= tableData.steps.size) {
			textLeft = ""
			textRight = ""
			return
		}

		val isBackwards = this::currentStep.isInitialized && currentStep.currentStep > currentStepPosition
		currentStep = tableData.steps[currentStepPosition]
		if (currentStep.currentWindowRow > currentCompletedWindowRow) {
			currentCompletedWindowRow++
			val previousStep = tableData.steps[currentStepPosition - 1]
			val result = "(${previousStep.windowResultStartPos}, ${tableData.sequence.substring(previousStep.windowResultStartPos, previousStep.windowResultStartPos + tableData.k)})"
			if (!minimizersList.values.contains(result)) {
				minimizersList[currentCompletedWindowRow] = result
			}
		}
		if (currentStep.currentWindowRow < currentCompletedWindowRow) currentCompletedWindowRow--

		if (valueAnimator.isAnimating() && currentStepPosition > 0) {
			valueAnimator.cancelAnimation()
			val previousStep = if (isBackwards) {
				tableData.steps[currentStepPosition + 1]
			} else {
				tableData.steps[currentStepPosition - 1]
			}

			currentPositionResultX = tableCellWidthFirst + (previousStep.currentMinKmerStartPos * tableCellWidth)
			currentPositionResultY = (previousStep.currentWindowRow + 1) * tableCellHeight
			currentPositionCompareX = (tableCellWidthFirst + (previousStep.nextKmerStartPos * tableCellWidth)) - 10
			currentPositionCompareY = ((previousStep.currentWindowRow + 1) * tableCellHeight) - 10

			invalidate()
		}

		valueAnimator.addFloatProperty("currentPositionResultX", currentPositionResultX, tableCellWidthFirst + (currentStep.currentMinKmerStartPos * tableCellWidth))
		valueAnimator.addFloatProperty("currentPositionResultY", currentPositionResultY, (currentStep.currentWindowRow + 1) * tableCellHeight)
		valueAnimator.addFloatProperty("currentPositionCompareX", currentPositionCompareX, (tableCellWidthFirst + (currentStep.nextKmerStartPos * tableCellWidth)) - 10)
		valueAnimator.addFloatProperty("currentPositionCompareY", currentPositionCompareY, ((currentStep.currentWindowRow + 1) * tableCellHeight) - 10)

		val middle = (currentPositionResultX + (currentPositionCompareX + (tableCellWidth *  tableData.k))) / 2
		val middleX = width / 2
		val middleY = height / 2

		val totalTableWidth = (tableCellWidthFirst + (tableCellWidth * (tableData.sequence.length + 1)))
		val totalTableHeight = (tableCellHeight * (tableData.maxWindowRow + 2 + (5 / scaleFactor)))

		val visibleWidth = width / scaleFactor
		val visibleHeight = height / scaleFactor

		val maxXOffset = max(0f, totalTableWidth - visibleWidth)
		val maxYOffset = max(0f, totalTableHeight - visibleHeight)

		valueAnimator.addFloatProperty("xOffset", xOffset, max(-maxXOffset, min(middleX - (middle), tableCellWidth)))
		valueAnimator.addFloatProperty("yOffset", yOffset, max(-maxYOffset, min(middleY - (currentPositionResultY + tableCellHeight), tableCellHeight)))

		valueAnimator.start()

		shouldAnimateBox = tableCellWidthFirst + (currentStep.nextKmerStartPos * tableCellWidth) + (tableData.k * tableCellWidth) <= (tableCellWidthFirst + (tableCellWidth * (tableData.sequence.length))) &&
				(currentStep.nextKmerStartPos + tableData.k) <= currentStep.currentWindowStartPos + currentStep.currentWindow.length

		if (shouldAnimateBox) {
			textLeft = tableData.sequence.substring(currentStep.currentMinKmerStartPos, currentStep.currentMinKmerStartPos + tableData.k)
			textRight = tableData.sequence.substring(currentStep.nextKmerStartPos, currentStep.nextKmerStartPos + tableData.k)
		} else {
			textLeft = ""
			textRight = ""
		}
	}

	private val animateRunnable: Runnable = Runnable {
		currentStepPosition++
		if (currentStepPosition > tableData.steps.size) currentStepPosition = tableData.steps.size
		calculateNexStepPositions()
		invalidate()
		autoPlayNext()
	}

	private fun autoPlayNext() {
		if (currentStepPosition >= tableData.steps.size) {
			togglePlay()
			return
		}
		autoPlayHandler.postDelayed(animateRunnable, autoPlayStepPauseDuration)
	}

	/**
	 * User animation control functions
	 */
	fun nextStep() {
		isAutoPlay = false
		autoPlayHandler.removeCallbacks(animateRunnable)
		currentStepPosition++
		if (currentStepPosition > tableData.steps.size) currentStepPosition = tableData.steps.size
		calculateNexStepPositions()
		invalidate()
	}
	fun previousStep() {
		isAutoPlay = false
		autoPlayHandler.removeCallbacks(animateRunnable)
		currentStepPosition--
		if (currentStepPosition < -1) currentStepPosition = -1
		calculateNexStepPositions()
		invalidate()
	}
	fun togglePlay(): Boolean {
		isAutoPlay = !isAutoPlay

		if (isAutoPlay) {
			autoPlayHandler.postDelayed(animateRunnable, 200L)
		} else {
			autoPlayHandler.removeCallbacks(animateRunnable)
		}

		return isAutoPlay
	}

	fun jumpToEnd() {
		currentStepPosition = tableData.steps.size
		currentCompletedWindowRow = tableData.maxWindowRow + 1

		minimizersList.clear()
		for (step in tableData.steps) {
			if (!step.hasNextKmer) {
				val result = "(${step.windowResultStartPos}, ${tableData.sequence.substring(step.windowResultStartPos, step.windowResultStartPos + tableData.k)})"
				if (!minimizersList.values.contains(result)) {
					minimizersList[step.currentWindowRow] = result
				}
			}
		}

		invalidate()
	}

	private fun copyPaint(original: Paint): Paint {
		val copy = Paint()
		copy.set(original)
		return copy
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

					if (!isAnimating) {
						xOffset += dx / scaleFactor
						yOffset += dy / scaleFactor
					}

					if (this::tableData.isInitialized) {
						val totalTableWidth = (tableCellWidthFirst + (tableCellWidth * (tableData.sequence.length + 1)))
						val totalTableHeight = (tableCellHeight * (tableData.maxWindowRow + 2 + (5 / scaleFactor)))

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
		}
		return true
	}

	companion object {

		@JvmStatic
		@BindingAdapter("android:tableData")
		fun TableView.setTableData(tableData: MinimizersTableData?) {
			if (tableData == null) return

			this.tableData = tableData
			this.completedRowsData.addAll(tableData.steps.filter { !it.hasNextKmer })

			val previousStep = tableData.steps[currentStepPosition + 1]
			currentPositionResultX = tableCellWidthFirst + (previousStep.currentMinKmerStartPos * tableCellWidth)
			currentPositionResultY = (previousStep.currentWindowRow + 1) * tableCellHeight
			currentPositionCompareX = (tableCellWidthFirst + (previousStep.nextKmerStartPos * tableCellWidth)) - 10
			currentPositionCompareY = ((previousStep.currentWindowRow + 1) * tableCellHeight) - 10

			calculateNexStepPositions()
			invalidate()
		}
	}
}