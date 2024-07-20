package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.SearchLocationRepository
import kotlinx.coroutines.launch

class SearchLocationViewModel(
    private val repository: SearchLocationRepository
) : ViewModel() {

    private val _location = MutableLiveData<List<Location>>()
    val location: LiveData<List<Location>> = _location

    private val _history = MutableLiveData<List<String>>()
    val history: LiveData<List<String>> = _history

    private val _searchInput = MutableLiveData<String>()
    val searchInput: LiveData<String> = _searchInput

    private val _markerLocation = MutableLiveData<Location>()
    val markerLocation: LiveData<Location> = _markerLocation

    init {
        _history.postValue(repository.getHistory())
    }

    fun searchLocationByHistory(locationName: String) {
        _searchInput.postValue(locationName)
    }

    fun searchLocation(category: String) {
        viewModelScope.launch {
            _location.postValue(repository.searchLocation(category))
        }
    }

    fun addHistory(locationName: String) {
        repository.addHistory(locationName)
        _history.postValue(repository.getHistory())
    }

    fun removeHistory(locationName: String) {
        repository.removeHistory(locationName)
        _history.postValue(repository.getHistory())
    }

    fun addMarker(location: Location) {
        _markerLocation.postValue(location)
    }
}