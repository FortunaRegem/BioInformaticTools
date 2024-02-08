package hr.fer.minimizertable.ui.result.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.minimizertable.model.MinimizersTableData
import hr.fer.minimizertable.ui.result.contract.MinimizerTableResultUseCaseContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class ResultViewModel(
	private var sequence: String,
	private var w: Int,
	private var k: Int,
	private var minimizerUseCase: MinimizerTableResultUseCaseContract
) : BaseViewModel() {

	var result: MutableLiveData<MinimizersTableData> = MutableLiveData()

	init {
		minimizerUseCase.getMinimizersResult(sequence, w, k)
			.onEach {
				result.postValue(it)
			}
			.catch {
				Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("ResultVM - getResults - %s", it.message)
			}
			.launchIn(viewModelScope)
	}
}