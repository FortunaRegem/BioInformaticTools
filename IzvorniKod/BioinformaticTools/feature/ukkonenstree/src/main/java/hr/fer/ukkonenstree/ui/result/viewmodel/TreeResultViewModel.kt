package hr.fer.ukkonenstree.ui.result.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.ukkonenstree.model.SuffixTreeData
import hr.fer.ukkonenstree.ui.result.contract.UkkonensTreeResultUseCaseContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class TreeResultViewModel(
	private var sequence: String,
	private var ukkonenUseCase: UkkonensTreeResultUseCaseContract
) : BaseViewModel() {

	var result: MutableLiveData<ArrayList<SuffixTreeData>> = MutableLiveData()

	val activeNode: MutableLiveData<String> = MutableLiveData("0")
	val activeEdge: MutableLiveData<String> = MutableLiveData("none")
	val activeLength: MutableLiveData<String> = MutableLiveData("0")
	val reminder: MutableLiveData<String> = MutableLiveData("0")

	init {
		ukkonenUseCase.calculateUkkonen(sequence)
			.onEach {
				result.postValue(it)
			}
			.catch {
				Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("ResultVM - getResults - %s", it.message)
			}
			.launchIn(viewModelScope)
	}
}