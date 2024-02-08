package hr.fer.fmindextable.ui.fmindex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.common.models.ConsumableLiveData
import hr.fer.fmindextable.ui.fmindex.contract.FMIndexTableUseCaseContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class FMIndexViewModel(
	private var fmIndexUseCase: FMIndexTableUseCaseContract
) : BaseViewModel() {

	var sequence: MutableLiveData<String> = MutableLiveData()
	var pattern: MutableLiveData<String> = MutableLiveData()

	var minimizingDone: ConsumableLiveData<Boolean> = ConsumableLiveData()
	var errorMessage: ConsumableLiveData<String> = ConsumableLiveData()

	fun calculate() {
		fmIndexUseCase.calculateFMIndex(
			sequence.value ?: "",
			pattern.value ?: "",
		)
			.onEach {
				minimizingDone.value = true
			}
			.catch {
				errorMessage.value = it.message
				Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("MinimizersVM - calculate - %s", it.message)
			}
			.launchIn(viewModelScope)
	}

}