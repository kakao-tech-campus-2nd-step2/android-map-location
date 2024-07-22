package campus.tech.kakao.map.ui.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.repository.LocationRepository
import campus.tech.kakao.map.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel
@Inject
constructor(
    private val locationRepository: LocationRepository,
) : ViewModel() {
    private val _location = MutableStateFlow(getDefaultLocation())
    val location: StateFlow<Location> get() = _location

    init {
        loadLocation()
    }

    fun saveLocation(newLocation: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                locationRepository.saveLocation(newLocation)
                _location.value = newLocation
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error saving location", e)
            }
        }
    }

    private fun loadLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loadedLocation = locationRepository.loadLocation()
                _location.value = loadedLocation
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error loading location", e)
                _location.value = getDefaultLocation()
            }
        }
    }

    private fun getDefaultLocation(): Location {
        return Location(
            name = "부산대 컴공관",
            latitude = 35.230934,
            longitude = 129.082476,
            address = "부산광역시 금정구 부산대학로 63번길 2",
        )
    }
}
