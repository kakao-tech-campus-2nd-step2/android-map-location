package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.Model.LocationData

class MapViewModel: ViewModel() {
    private val _locationData = MutableLiveData<List<LocationData>>()
    val locationData: LiveData<List<LocationData>> get() = _locationData
    fun setMapViewAdapter(locationData: List<LocationData>) {
        _locationData.value = locationData
    }
}