package hr.fer.common.extensions

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import hr.fer.common.utils.ScreenUtils

fun Activity.snack(view: View, message: String?, length: Int = Snackbar.LENGTH_SHORT,
				   anchor: View? = null, marginTop: Float? = null, isTop: Boolean? = false): Snackbar {
	return Snackbar.make(this, view, message.orEmpty(), length)
		.apply {
			if(isTop == false) {
				this.anchorView = anchor
			} else if (isTop == true) {
				(this.view.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.TOP
				this.view.translationY = marginTop ?: ScreenUtils.pxToDp(500).toFloat()
			}
		}
}