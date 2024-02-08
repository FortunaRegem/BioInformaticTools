package hr.fer.common.base

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fer.common.models.NavigationCommand
import hr.fer.common.view.popup.PermissionDeniedDialog
import hr.fer.common.view.popup.RationaleDialog

abstract class BaseFragment<BINDING: ViewDataBinding, VM: BaseViewModel>(
	private val bindViewModelBR: Int = -1,
	private val bindFragmentBR: Int = -1
): Fragment() {

	@get:LayoutRes
	protected abstract val layoutId: Int
	protected abstract val viewModel: VM
	protected lateinit var binding: BINDING

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)

		binding.apply {
			lifecycleOwner = viewLifecycleOwner
			try {
				if (bindViewModelBR != -1) {
					setVariable(bindViewModelBR, viewModel)
				}
			} catch (e: Exception) {
				throw Exception("No binding variableId found with name viewModel for: ${viewModel::class.java.name}.")
			}
			try {
				if (bindFragmentBR != -1) {
					setVariable(bindFragmentBR, this@BaseFragment)
				}
			} catch (e: Exception) {
				throw Exception("No binding variableId found with name fragment for: ${this@BaseFragment::class.java.name}.")
			}
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		observeNavigation()
	}

	private fun observeNavigation() {
		viewModel.navigation.observe(viewLifecycleOwner) {
			when (it) {
				is NavigationCommand.ToDirection -> {
					if (it.extras != null) {
						findNavController().navigate(it.directions, it.extras)
					} else {
						findNavController().navigate(it.directions)
					}
				}
				is NavigationCommand.Back -> findNavController().navigateUp()
			}
		}
	}

	open fun onPermissionsResultCallback(permissionResult: Pair<String, Boolean>) {
		throw Throwable("Must override this method to handle permission results.")
	}

	private val requestMultiplePermissionsLauncher = registerForActivityResult(
		ActivityResultContracts.RequestMultiplePermissions()
	) { permissions ->
		permissions.entries.forEach {
			onPermissionsResultCallback(Pair(it.key, it.value))
		}
	}

	private fun shouldShowRationale(permission: String) =
		ActivityCompat.shouldShowRequestPermissionRationale(
			requireActivity(),
			permission
		)

	fun hasPermission(permission: String) = isPermissionGranted(permission)

	private fun isPermissionGranted(permission: String) =
		ContextCompat.checkSelfPermission(
			requireContext(),
			permission
		) == PackageManager.PERMISSION_GRANTED

	fun requestPermissions(
		vararg permissions: String
	) {
		val permissionArray: ArrayList<String> = ArrayList()
		for (permission in permissions) {
			if (isPermissionGranted(permission)) {
				onPermissionsResultCallback(Pair(permission, true))
			} else {
				permissionArray.add(permission)
			}
		}

		requestMultiplePermissionsLauncher.launch(permissionArray.toTypedArray())
	}

	fun showDisabledPermissionDialog(
		permission: String,
		@StringRes userMessage: Int,
		@StringRes permissionRequiredToast: Int,
		@StringRes permissionDeniedMessage: Int,
		turnOnInSettingsListener: DialogInterface.OnClickListener? = null,
		onDismissListener: (() -> Unit)? = null
	): Boolean {
		return if (shouldShowRationale(permission)) {
			RationaleDialog(
				permission,
				userMessage,
				permissionRequiredToast,
				requestMultiplePermissionsLauncher,
				onDismissListener
			)
				.show(requireActivity().supportFragmentManager, "RationaleDialog")
			true
		} else {
			PermissionDeniedDialog(permissionDeniedMessage, turnOnInSettingsListener, onDismissListener)
				.show(requireActivity().supportFragmentManager, "PermissionDeniedDialog")
			false
		}
	}

}