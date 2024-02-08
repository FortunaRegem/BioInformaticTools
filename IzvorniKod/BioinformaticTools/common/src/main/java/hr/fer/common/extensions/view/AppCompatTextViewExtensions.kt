package hr.fer.common.extensions.view

import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:text")
fun AppCompatTextView.setStringResText(@StringRes stringResId: Int?) {
	if (stringResId != null && stringResId != 0) {
		this.setText(stringResId)
	}
}