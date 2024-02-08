package hr.fer.sequencealigner.ui.aligner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.common.models.BindableItem
import hr.fer.sequencealigner.model.toBindableItem
import hr.fer.sequencealigner.ui.aligner.contract.AlignUseCaseContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class ResultViewModel(
	private var alignUseCase: AlignUseCaseContract
) : BaseViewModel() {

	var resultList: MutableLiveData<ArrayList<BindableItem>> = MutableLiveData()
	var resultDistance: MutableLiveData<String> = MutableLiveData()

	fun getResults(sequenceA: String, sequenceB: String) {
		alignUseCase.getAlignmentResults(sequenceA, sequenceB)
			.onEach {
				resultDistance.value = it.second.toString()
				resultList.value = it.first.map { item ->
					item.toBindableItem()
				} as ArrayList<BindableItem>
			}
			.catch {
				Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("ResultVM - getResults - %s", it.message)
			}
			.launchIn(viewModelScope)
	}
}