package hr.fer.common.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View

object BitmapHelper {

	fun drawableToBitmap(drawable: Drawable?): Bitmap? {
		if (drawable is BitmapDrawable) {
			return drawable.bitmap
		}
		var width = drawable?.intrinsicWidth ?: 0
		width = if (width > 0) width else 1
		var height = drawable?.intrinsicHeight ?: 0
		height = if (height > 0) height else 1
		val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)
		drawable?.setBounds(0, 0, canvas.width, canvas.height)
		drawable?.draw(canvas)
		return bitmap
	}

	fun viewToBitmap(view: View): Bitmap {
		val returnedBitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(returnedBitmap)
		if (view.background != null)
			view.background.draw(canvas)
		else
			canvas.drawColor(Color.WHITE)
		view.draw(canvas)

		return returnedBitmap
	}

}