package hr.fer.common.extensions.view

import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:tint")
fun AppCompatImageView.setTint(@ColorInt color: Int) {
	this.setColorFilter(color)
}