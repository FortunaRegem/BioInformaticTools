package hr.fer.common.models.enums

import androidx.annotation.StringRes
import hr.fer.common.R

//TODO add background dimming or blur
enum class PopupEnumModel(
	@StringRes var title: Int? = null,
	@StringRes var description: Int? = null,
	@StringRes var firstActionText: Int? = null,
	@StringRes var secondActionText: Int? = null,
	var cancelable: Boolean = true,
	var firstButtonTheme: Int? = null,
	var secondButtonTheme: Int? = null,
	private var firstAction: () -> Unit = {},
	private var secondAction: () -> Unit = {},
) {

	//example change when needed
	SKIP_ONBOARDING(
		title = R.string.app_name,
		description = R.string.turn_on,
		firstActionText = R.string.yes,
		secondActionText = R.string.no
	);

	fun onActionClick(action: PopupActionEnum) {
		when (action) {
			PopupActionEnum.FIRST_ACTION -> firstAction()
			PopupActionEnum.SECOND_ACTION -> secondAction()
		}
	}

	fun setActions(
		firstAction: (() -> Unit)? = null,
		secondAction: (() -> Unit)? = null
	) {
		firstAction?.let { this.firstAction = firstAction }
		secondAction?.let { this.secondAction = secondAction }
	}
}

enum class PopupActionEnum {
	FIRST_ACTION,
	SECOND_ACTION
}