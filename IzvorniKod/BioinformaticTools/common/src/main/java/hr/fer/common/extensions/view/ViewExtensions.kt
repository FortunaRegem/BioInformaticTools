package hr.fer.common.extensions.view

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import hr.fer.common.utils.ScreenUtils

fun View.snack(
	message: String?,
	length: Int = Snackbar.LENGTH_SHORT,
	anchor: View? = null,
	marginBottom: Float? = null,
	marginTop: Float? = null,
	isTop: Boolean = false
): Snackbar {
	return Snackbar.make(context, this, message.orEmpty(), length)
		.apply {
			if(anchor != null) {
				this.anchorView = anchor
			}
			if (isTop) {
				(this.view.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.TOP
				this.view.translationY = marginTop ?: if (anchor != null) 0f else ScreenUtils.dpToPx(60f).toFloat()
			} else {
				this.view.translationY = - (marginBottom ?: if (anchor != null) 0f else ScreenUtils.dpToPx(20f).toFloat())
			}
		}
}
