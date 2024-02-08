package hr.fer.common.view.popup

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hr.fer.common.R

class PermissionDeniedDialog(
	@StringRes private val permissionDeniedMessage: Int,
	private val turnOnInSettingsListener: DialogInterface.OnClickListener? = null,
	private var onDismissListener: (() -> Unit)? = null
): DialogFragment() {

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = AlertDialog.Builder(requireContext())
			.setMessage(permissionDeniedMessage)
			.setPositiveButton(android.R.string.ok, null)

		if (turnOnInSettingsListener != null) {
			builder
				.setNeutralButton(R.string.turn_on, turnOnInSettingsListener)
		}

		return builder.create()
	}

	override fun onDismiss(dialog: DialogInterface) {
		onDismissListener?.invoke()
		super.onDismiss(dialog)
	}
}