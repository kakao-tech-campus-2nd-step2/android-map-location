package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.SearchLocationRepository
import kotlinx.coroutines.launch

class SearchLocationViewModel : ViewModel() {
    private lateinit var repository: SearchLocationRepository
    fun setRepository(repository: SearchLocationRepository) {
        this.repository = repository
        _history.value = repository.getHistory()
    }

    private val _location = MutableLiveData<List<Location>>()
    val location: LiveData<List<Location>> = _location

    private val _history = MutableLiveData<List<String>>()
    val history: LiveData<List<String>> = _history

    fun searchLocation(category: String) {
        viewModelScope.launch {
            _location.value = repository.searchLocation(category)
        }
    }

    fun addHistory(locationName: String) {
        repository.addHistory(locationName)
        _history.value = repository.getHistory()
    }

    fun removeHistory(locationName: String) {
        repository.removeHistory(locationName)
        _history.value = repository.getHistory()
    }
}