package hr.fer.fmindextable.ui.result.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.fmindextable.model.FMIndexTableData
import hr.fer.fmindextable.ui.result.contract.FmIndexTableResultUseCaseContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class ResultViewModel(
	private var sequence: String,
	private var pattern: String,
	private var fmIndexUseCase: FmIndexTableResultUseCaseContract
) : BaseViewModel() {

	var result: MutableLiveData<FMIndexTableData> = MutableLiveData()
	var currentStepDescription: MutableLiveData<String> = MutableLiveData()

	init {
		fmIndexUseCase.getFMIndexResult(sequence, pattern)
			.onEach {
				result.postValue(it)
			}
			.catch {
				Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("ResultVM - getResults - %s", it.message)
			}
			.launchIn(viewModelScope)
	}
}