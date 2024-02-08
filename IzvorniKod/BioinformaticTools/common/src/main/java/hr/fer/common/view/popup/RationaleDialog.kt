package hr.fer.common.view.popup

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment

class RationaleDialog(
	private val permission: String,
	@StringRes private val userMessage: Int,
	@StringRes private val permissionRequiredToast: Int,
	private val requestMultiplePermissionsLauncher: ActivityResultLauncher<Array<String>>,
	private var onDismissListener: (() -> Unit)? = null
): DialogFragment() {

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return AlertDialog.Builder(requireContext())
			.setMessage(userMessage)
			.setPositiveButton(android.R.string.ok) { _, _ ->
				requestMultiplePermissionsLauncher.launch(listOf(permission).toTypedArray())
			}
			.setNegativeButton(android.R.string.cancel, null)
			.create()
	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)
		Toast.makeText(
			activity,
			permissionRequiredToast,
			Toast.LENGTH_SHORT
		)
			.show()

		onDismissListener?.invoke()
	}
}