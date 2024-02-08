package hr.fer.common.utils

import android.animation.ValueAnimator
import androidx.core.animation.doOnEnd

class MultiPropertyAnimator<T>(
	private val duration: Long,
	private val updateListener: (Map<T, Any>) -> Unit,
	private val endListener: () -> Unit
) {
	private val properties = mutableMapOf<T, AnimatableProperty>()
	private lateinit var animator: ValueAnimator
	private var isAnimating: Boolean = false

	fun addFloatProperty(key: T, startValue: Float, endValue: Float): MultiPropertyAnimator<T> {
		properties[key] = AnimatableProperty.FloatProperty(startValue, endValue)
		return this
	}

	fun addIntProperty(key: T, startValue: Int, endValue: Int): MultiPropertyAnimator<T> {
		properties[key] = AnimatableProperty.IntProperty(startValue, endValue)
		return this
	}

	fun start() {
		isAnimating = true
		animator = ValueAnimator.ofFloat(0f, 1f).apply {
			this.duration = this@MultiPropertyAnimator.duration
			addUpdateListener { animator ->
				val fraction = animator.animatedValue as Float
				val currentValues = properties.mapValues { interpolate(it.value, fraction) }
				updateListener(currentValues)
			}
			doOnEnd {
				isAnimating = false
				endListener()
				properties.clear()
			}
			start()
		}
	}

	fun cancelAnimation() {
		if (isAnimating) {
			animator.cancel()
			isAnimating = false
			properties.clear()
		}
	}

	fun isAnimating() = isAnimating

	private fun interpolate(property: AnimatableProperty, fraction: Float): Any {
		return when (property) {
			is AnimatableProperty.FloatProperty -> {
				property.start + (property.end - property.start) * fraction
			}
			is AnimatableProperty.IntProperty -> {
				(property.start + (property.end - property.start) * fraction).toInt()
			}
		}
	}
}

sealed class AnimatableProperty {
	data class FloatProperty(val start: Float, val end: Float) : AnimatableProperty()
	data class IntProperty(val start: Int, val end: Int) : AnimatableProperty()
}