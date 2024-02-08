package hr.fer.ukkonenstree.ui.ukkonenstree.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.common.models.ConsumableLiveData
import hr.fer.ukkonenstree.ui.ukkonenstree.contract.MinimizerTableUseCaseContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class UkkonensTreeViewModel() : BaseViewModel() {

	var sequence: MutableLiveData<String> = MutableLiveData()

	var errorMessage: ConsumableLiveData<String> = ConsumableLiveData()

//	fun calculate() {
//		minimizerUseCase.calculateMinimizers(
//			sequence.value ?: "",
//			w.value?.toInt() ?: 0,
//			k.value?.toInt() ?: 0,
//		)
//			.onEach {
//				minimizingDone.value = true
//			}
//			.catch {
//				errorMessage.value = it.message
//				Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("MinimizersVM - calculate - %s", it.message)
//			}
//			.launchIn(viewModelScope)
//	}

}