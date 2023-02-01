package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm

import androidx.lifecycle.ViewModel
import java.util.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.reflect.Array.get
import java.util.UUID

class GlucoseDetailViewModel(glucoseID: Date) : ViewModel() {
    private val glucoseRepository = GlucoseRepository.get()

    private val _glucose: MutableStateFlow<Glucose?> = MutableStateFlow(null)
    val glucose: StateFlow<Glucose?> = _glucose.asStateFlow()

    init {
        viewModelScope.launch {
            _glucose.value = glucoseRepository.getGlucose(glucoseID)
        }
    }

    fun getGlucose(glucoseID: Date): Int {
        viewModelScope.launch {
            _glucose.value = glucoseRepository.getGlucose(glucoseID)
        }
        if (glucoseID == _glucose.value?.date)
        {
            return 0
        }
        else
        {
            return -1
        }
    }

    fun updateGlucose(onUpdate: (Glucose) -> Glucose) {
        _glucose.update { oldGlucose ->
            oldGlucose?.let { onUpdate(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
            glucose.value?.let { glucoseRepository.updateGlucose(it) }
    }

    fun deleteGlucose(glucoseID: Date) {
        viewModelScope.launch {
            glucoseRepository.deleteGlucose(glucoseRepository.getGlucose(glucoseID))
        }
    }
}




    class GlucoseDetailViewModelFactory(
        private val glucoseID: Date
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GlucoseDetailViewModel(glucoseID) as T
        }
    }
