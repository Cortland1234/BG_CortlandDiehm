package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm

import androidx.lifecycle.ViewModel
import java.util.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.reflect.Array.get

class GlucoseListViewModel : ViewModel() {

    private val glucoseRepository = GlucoseRepository.get()

    private val _glucoses: MutableStateFlow<List<Glucose>> = MutableStateFlow(emptyList())
    val glucoses: StateFlow<List<Glucose>>
        get() = _glucoses.asStateFlow()

    init {
        viewModelScope.launch {
            glucoseRepository.getGlucoses().collect {
                _glucoses.value = it
            }
        }
    }

    suspend fun addGlucose(glucose: Glucose) {
        glucoseRepository.addGlucose(glucose)
    }

}