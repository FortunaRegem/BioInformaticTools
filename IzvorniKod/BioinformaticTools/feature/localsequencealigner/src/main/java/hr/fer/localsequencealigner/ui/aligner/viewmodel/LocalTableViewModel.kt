package hr.fer.localsequencealigner.ui.aligner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fer.common.base.BaseViewModel
import hr.fer.common.constants.DEFAULT_VIEW_MODEL_ERROR_TAG
import hr.fer.common.models.ConsumableLiveData
import hr.fer.common.models.sequencealigner.AlignmentResultItem
import hr.fer.common.models.sequencealigner.TableItem
import hr.fer.localsequencealigner.ui.aligner.contract.AlignLocalUseCaseContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class LocalTableViewModel(
	private val sequenceA: String,
	private val sequenceB: String,
	private val alignUseCase: AlignLocalUseCaseContract
) : BaseViewModel() {

	val tableData: ConsumableLiveData<TableItem> = ConsumableLiveData()
	val dataHasBeenSet: ConsumableLiveData<Boolean> = ConsumableLiveData()
	val currentResult: MutableLiveData<AlignmentResultItem> = MutableLiveData()

	init {
		alignUseCase.getTableLocal(sequenceA, sequenceB)
			.onEach {
				tableData.value = it
				dataHasBeenSet.postValue(true)
			}
			.catch {
				Timber.tag(DEFAULT_VIEW_MODEL_ERROR_TAG).d("LocalTableVM - init - %s", it.message)
			}
			.launchIn(viewModelScope)
	}

}
