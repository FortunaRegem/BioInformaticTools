package hr.fer.common.utils

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

object CameraUtils {

	fun Context?.findMaxZoomLevel(lensFacingDirection: Int): Pair<String, Float?>? {
		val cameraManager = this?.getSystemService(Context.CAMERA_SERVICE) as CameraManager

		try {
			val cameraIds = cameraManager.cameraIdList

			for (cameraId in cameraIds) {
				val characteristics = cameraManager.getCameraCharacteristics(cameraId)
				val facing = characteristics.get(CameraCharacteristics.LENS_FACING)

				if (facing == lensFacingDirection) {
					return Pair(cameraId, characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM))
				}
			}
		} catch (e: CameraAccessException) {
			return null
		}

		return null
	}
}