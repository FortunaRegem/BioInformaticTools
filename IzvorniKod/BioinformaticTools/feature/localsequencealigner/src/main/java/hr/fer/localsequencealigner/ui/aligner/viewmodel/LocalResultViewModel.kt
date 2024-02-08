package hr.fer.localsequencealigner.ui.aligner.viewmodel

import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.common.models.BindableItem
import hr.fer.common.models.ConsumableLiveData
import hr.fer.localsequencealigner.model.toBindableItem
import hr.fer.localsequencealigner.ui.aligner.contract.AlignLocalUseCaseContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class LocalResultViewModel(
	private var alignUseCase: AlignLocalUseCaseContract
) : BaseViewModel() {

	var resultList: ConsumableLiveData<ArrayList<BindableItem>> = ConsumableLiveData()
	var resultDistance: ConsumableLiveData<String> = ConsumableLiveData()

	fun getResults(sequenceA: String, sequenceB: String) {
		alignUseCase.getAlignmentResultsLocal(sequenceA, sequenceB)
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