package hr.fer.common.extensions

import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.getStringFromResources(value: Int): String =
	if (value != -1)
		try {
			context?.resources?.getString(value).orEmpty()
		} catch (e: Exception) {
			""
		}
	else ""

fun Fragment.snack(message: String?, length: Int = Snackbar.LENGTH_SHORT,
				   anchor: View? = null, marginTop: Float? = null, isTop: Boolean? = false) =
	this.requireActivity().snack(
		requireView(),
		message,
		length,
		anchor,
		marginTop,
		isTop
	)

fun Fragment.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) = toast(
	getString(message),
	duration
)

fun Fragment.toast(message: String?, duration: Int = Toast.LENGTH_SHORT): Toast? {
	activity?.let {
		return Toast.makeText(it, message, duration)
	}
	return null
}