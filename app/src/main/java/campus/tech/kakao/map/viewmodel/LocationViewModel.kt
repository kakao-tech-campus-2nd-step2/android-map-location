package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.repository.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _locations = MutableLiveData<List<Location>>()

    private val _searchedLocations = MutableLiveData<List<Location>>()
    val searchedLocations: LiveData<List<Location>> get() = _searchedLocations

    fun setLocationsFromDB() {
        _locations.value = locationRepository.getLocationLocal()
        _searchedLocations.value = emptyList()
    }

    private fun getSearchedLocationsSize(): Int {
        return _searchedLocations.value?.size ?: 0
    }


    fun searchLocationsFromDB(query: String): Int {
        return locationRepository.searchLocationLocal(query).size
    }

    fun addLocationFromDB() {
        locationRepository.addLocationLocal()
    }

    fun setLocationsFromKakaoAPI() {
        _locations.value = emptyList()
        _searchedLocations.value = emptyList()
    }

    fun searchLocationsFromKakaoAPI(query: String, deleteNoResultMessageCallback: (Int) -> Unit) {
        viewModelScope.launch {
            _searchedLocations.value = locationRepository.getLocationRemote(query)
            deleteNoResultMessageCallback(getSearchedLocationsSize())
        }
    }
}