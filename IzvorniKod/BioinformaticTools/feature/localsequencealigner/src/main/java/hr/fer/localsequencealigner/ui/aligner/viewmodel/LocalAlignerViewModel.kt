package hr.fer.localsequencealigner.ui.aligner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.common.models.ConsumableLiveData
import hr.fer.localsequencealigner.ui.aligner.contract.AlignLocalUseCaseContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import timber.log.Timber

class LocalAlignerViewModel(
	private var alignUseCase: AlignLocalUseCaseContract
) : BaseViewModel() {

	var sequenceA: MutableLiveData<String> = MutableLiveData()
	var sequenceB: MutableLiveData<String> = MutableLiveData()
	var match: MutableLiveData<String> = MutableLiveData()
	var mismatch: MutableLiveData<String> = MutableLiveData()
	var gap: MutableLiveData<String> = MutableLiveData()

	var alignmentDone: ConsumableLiveData<Boolean> = ConsumableLiveData()
	var errorMessage: ConsumableLiveData<String> = ConsumableLiveData()

	fun match() {
		alignUseCase.alignLocal(
			sequenceA.value ?: "",
			sequenceB.value ?: "",
			match.value?.toInt() ?: 0,
			mismatch.value?.toInt() ?: 0,
			gap.value?.toInt() ?: 0
		)
			.onEach {
				alignmentDone.value = true
			}
			.catch {
				errorMessage.value = it.message
				Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("AlignerVM - match - %s", it.message)
			}
			.launchIn(viewModelScope)
	}

	fun initData(sequenceA: String, sequenceB: String) {
		alignUseCase.getTableLocal(sequenceA, sequenceB)
			.take(1)
			.onEach {
				this.sequenceA.value = it.tableParameters.sequenceA
				this.sequenceB.value = it.tableParameters.sequenceB
				this.match.value = it.tableParameters.match.toString()
				this.mismatch.value = it.tableParameters.mismatch.toString()
				this.gap.value = it.tableParameters.gap.toString()
			}
			.catch {
					Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("AlignerVM - initData - %s", it.message)
			}
			.launchIn(viewModelScope)
	}

}