package am.solidy.flowsummator

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val _summatorInputValue = mutableStateOf(TextFieldValue(""))
    val summatorInputValue: State<TextFieldValue> = _summatorInputValue

    fun setInputValue(value: TextFieldValue) {
        _summatorInputValue.value = value
    }

    private val _summatorResult = MutableStateFlow<List<Int>>(mutableListOf())
    val summatorResult = _summatorResult.asStateFlow()


    private var summatorJob: Job? = null
    fun startSummator() {
        summatorJob?.cancel()
        summatorJob = viewModelScope.launch {
            val flowsCount = summatorInputValue.value.text.toInt()
            val listOfFlow = mutableListOf<Flow<Int>>()
            repeat(flowsCount) { index ->
                val currentFlow = flow {
                    val value = index + 1
                    delay(FLOW_EMMIT_DELAY_TIME * value)
                    emit(value)
                }
                listOfFlow.add(currentFlow)
            }

            //first approach
            listOfFlow.merge().fold(0) { a, b ->
                val sumOfEmmitingValue = a + b
                updateSummatorResult(sumOfEmmitingValue)
                sumOfEmmitingValue
            }

            //second approach
//            channelFlow {
//                listOfFlow.forEach { flow ->
//                    launch {
//                        flow.onEach { send(it) }.launchIn(this)
//                    }
//                }
//            }.fold(0) { a, b ->
//                val sumOfEmmitingValue = a + b
//                updateSummatorResult(sumOfEmmitingValue)
//                sumOfEmmitingValue
//            }
        }
    }

    private fun updateSummatorResult(item: Int) {
        _summatorResult.update {
            val newList = it.toMutableList()
            newList.add(item)
            newList
        }
    }

    override fun onCleared() {
        summatorJob?.cancel()
        super.onCleared()
    }

    companion object {
        private const val FLOW_EMMIT_DELAY_TIME = 1000L
    }
}