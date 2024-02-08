package hr.fer.common.view.popup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import hr.fer.common.R
import hr.fer.common.databinding.PopupBinding
import hr.fer.common.models.enums.PopupActionEnum
import hr.fer.common.models.enums.PopupEnumModel
import hr.fer.common.constants.DIM_40_PERCENT

class Popup(private val model: PopupEnumModel) : DialogFragment() {

	lateinit var binding: PopupBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = DataBindingUtil.inflate<PopupBinding>(inflater, R.layout.popup, container, false)
		binding.apply {
			popupModel = this@Popup.model
			popup = this@Popup
		}
		dialog?.setCanceledOnTouchOutside(model.cancelable)
		return binding.root
	}
	fun show(manager: FragmentManager) {
		if (!this.isVisible) {
			show(manager, tag)
		} else return
	}

	fun dismiss(action: PopupActionEnum) {
		model.onActionClick(action)
		if (model.cancelable) dismiss()
	}

	override fun onStart() {
		super.onStart()

		val window: Window? = dialog?.window
		val windowParams: WindowManager.LayoutParams = window?.attributes ?: return

		windowParams.dimAmount = DIM_40_PERCENT
		windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND

		window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		window.attributes = windowParams
	}
}