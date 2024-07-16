package ksc.campus.tech.kakao.map.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraPosition

data class LocationInfo(val address:String, val name:String, val latitude:Double, val longitude:Double){

}

class MapViewRepository {
    private val _selectedLocation: MutableLiveData<LocationInfo?> = MutableLiveData<LocationInfo?>(null)
    private val _cameraPosition: MutableLiveData<CameraPosition> = MutableLiveData(initialCameraPosition)

    val selectedLocation: LiveData<LocationInfo?>
        get() = _selectedLocation

    val cameraPosition: LiveData<CameraPosition>
        get() = _cameraPosition

    private fun getZoomCameraPosition(latitude: Double, longitude: Double) = CameraPosition.from(latitude, longitude, 18, 0.0,0.0, -1.0)

    fun updateSelectedLocation(locationInfo: LocationInfo){
        _selectedLocation.postValue(locationInfo)
    }

    fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double){
        _cameraPosition.postValue(getZoomCameraPosition(latitude, longitude))
    }

    fun updateCameraPosition(position: CameraPosition){
        _cameraPosition.postValue(position)
    }

    fun clearSelectedLocation(){
        _selectedLocation.postValue(null)
    }

    companion object {
        private var _instance: MapViewRepository? = null
        fun getInstance(): MapViewRepository {
            if (_instance == null) {
                _instance = MapViewRepository()
            }
            return _instance as MapViewRepository
        }

        val initialCameraPosition: CameraPosition = CameraPosition.from(35.8905341232321, 128.61213266480294, 15, 0.0,0.0, -1.0)
    }
}