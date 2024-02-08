package hr.fer.common.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import com.google.firebase.crashlytics.FirebaseCrashlytics
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.common.models.FlowResult
import hr.fer.common.models.NavigationCommand
import hr.fer.common.utils.ConsumableLiveData
import timber.log.Timber

open class BaseViewModel: ViewModel() {

    var navigation: ConsumableLiveData<NavigationCommand> = ConsumableLiveData()
    var flowResult: ConsumableLiveData<FlowResult> = ConsumableLiveData()

    fun navigate(navDirections: NavDirections, extras: Navigator.Extras? = null) {
        navigation.value = NavigationCommand.ToDirection(navDirections, extras)
    }

    open fun navigateBack() {
        navigation.value = NavigationCommand.Back
    }

    fun handleError(throwable: Throwable, method: String) {
        flowResult.value = FlowResult.Error(throwable.message)
        Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("%s%s", "$method: ", throwable.message)
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    override fun onCleared() {
        super.onCleared()
    }
}