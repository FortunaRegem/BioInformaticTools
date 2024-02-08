package hr.fer.common.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


abstract class BaseBottomSheet : BottomSheetDialogFragment() {

  private var onDismissListener: (() -> Unit)? = null
  fun setOnDismissListener(listener: () -> Unit) {
    onDismissListener = listener
  }

  private var onShowListener: (() -> Unit)? = null
  fun setOnShowListener(listener: () -> Unit) {
    onShowListener = listener
  }

  override fun dismiss() {
    lifecycleScope.launch {
      onDismissListener?.invoke()
      super.dismiss()
    }
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    onShowListener?.invoke()

    return super.onCreateDialog(savedInstanceState)
  }

  override fun show(manager: FragmentManager, tag: String?) {
    onShowListener?.invoke()
    super.show(manager, tag)
  }

  override fun onDismiss(dialog: DialogInterface) {
    onDismissListener?.invoke()
    super.onDismiss(dialog)
  }

}