package hr.fer.common.extensions.view

import android.view.ContextThemeWrapper
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import hr.fer.common.R

@BindingAdapter("newTheme")
fun MaterialButton.setupNewTheme(theme: Int?) {
	theme?.let {
		this.let {
			val context = ContextThemeWrapper(
				it.context,
				theme
			)

			val drawable = ResourcesCompat.getDrawable(
				resources,
				R.drawable.background_ripple,
				context.theme
			)
			it.background = drawable
			it.invalidate()
		}
	}
}