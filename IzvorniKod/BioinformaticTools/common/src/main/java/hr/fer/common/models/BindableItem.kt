package hr.fer.common.models

import androidx.annotation.LayoutRes

data class BindableItem(
	val data: Any,
	@LayoutRes val layoutId: Int,
	val variableId: Int
)