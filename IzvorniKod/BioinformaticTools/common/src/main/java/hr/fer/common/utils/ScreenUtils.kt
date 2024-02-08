package hr.fer.common.utils

import android.content.res.Resources

object ScreenUtils {
	fun dpToPx(dp: Float): Int {
		return (dp * Resources.getSystem().displayMetrics.density).toInt()
	}

	fun pxToDp(px: Int): Int {
		return (px / Resources.getSystem().displayMetrics.density).toInt()
	}

	fun pxToSp(px: Int): Float {
		return (px / Resources.getSystem().displayMetrics.scaledDensity)
	}

	fun spToPx(sp: Float): Float {
		return (sp * Resources.getSystem().displayMetrics.scaledDensity)
	}
}