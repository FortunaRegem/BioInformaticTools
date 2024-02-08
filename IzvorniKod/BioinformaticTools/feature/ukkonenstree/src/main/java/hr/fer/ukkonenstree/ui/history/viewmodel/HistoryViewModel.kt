package hr.fer.ukkonenstree.ui.history.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.models.BindableItem
import hr.fer.ukkonenstree.ui.history.contract.HistoryUseCaseContract
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion

class HistoryViewModel(
	private var historyUseCase: HistoryUseCaseContract
) : BaseViewModel() {

	var resultList: MutableLiveData<ArrayList<BindableItem>> = MutableLiveData()
//	var itemClicked: ConsumableLiveData<MinimizersTableHistoryData> = ConsumableLiveData()

	var errorMessage: MutableLiveData<String> = MutableLiveData("")

	init {
		getResults()
	}

	private fun getResults() {
//		historyUseCase.getHistory()
//			.onEach {
//				resultList.value = it.map { item ->
//					item.onClick = this::onItemClicked
//					item.toBindableItem()
//				} as ArrayList<BindableItem>
//			}
//			.catch {
//				Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("HistoryVM - getResults - %s", it.message)
//			}
//			.launchIn(viewModelScope)
	}

	fun clearHistory() {
		historyUseCase.deleteHistory()
			.onCompletion {

			}
			.launchIn(viewModelScope)
	}

//	fun onItemClicked(tableModel: MinimizersTableHistoryData) {
//		itemClicked.value = tableModel
//	}
}