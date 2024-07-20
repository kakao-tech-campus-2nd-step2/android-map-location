package ksc.campus.tech.kakao.map.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.camera.CameraPosition
import kotlinx.serialization.Serializable
import ksc.campus.tech.kakao.map.models.datasources.MapPreferenceLocalDataSource


@Serializable
data class LocationInfo(val address:String, val name:String, val latitude:Double, val longitude:Double)

class MapViewRepository() {
    private val _selectedLocation: MutableLiveData<LocationInfo?> = MutableLiveData<LocationInfo?>(null)
    private val _cameraPosition: MutableLiveData<CameraPosition> = MutableLiveData(initialCameraPosition)

    private val mapPreferenceDataSource: MapPreferenceLocalDataSource = MapPreferenceLocalDataSource()
    val selectedLocation: LiveData<LocationInfo?>
        get() = _selectedLocation

    val cameraPosition: LiveData<CameraPosition>
        get() = _cameraPosition

    private fun getZoomCameraPosition(latitude: Double, longitude: Double) = CameraPosition.from(latitude, longitude, 18, 0.0,0.0, -1.0)

    private fun saveCurrentPositionToSharedPreference(context:Context, position: CameraPosition){
        mapPreferenceDataSource.saveCameraPosition(context, position)
    }

    private fun saveSelectedLocation(context:Context, location: LocationInfo){
        mapPreferenceDataSource.saveSelectedLocation(context, location)
    }

    private fun loadSavedCurrentPosition(context:Context): CameraPosition {
        val data = mapPreferenceDataSource.getCameraPosition(context)
        return data?: initialCameraPosition
    }

    private fun loadSavedSelectedLocation(context:Context): LocationInfo? {
        return mapPreferenceDataSource.getSelectedLocation(context)
    }

    fun loadFromSharedPreference(context:Context){
        val cameraPosition = loadSavedCurrentPosition(context)
        val selectedLocation = loadSavedSelectedLocation(context)

        updateCameraPosition(context, cameraPosition)
        if(selectedLocation != null)
            updateSelectedLocation(context, selectedLocation)
    }

    fun updateSelectedLocation(context:Context, locationInfo: LocationInfo){
        saveSelectedLocation(context, locationInfo)
        _selectedLocation.postValue(locationInfo)
    }

    fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double){
        _cameraPosition.postValue(getZoomCameraPosition(latitude, longitude))
    }

    fun updateCameraPosition(context:Context, position: CameraPosition){
        saveCurrentPositionToSharedPreference(context, position)
        _cameraPosition.postValue(position)
    }

    fun clearSelectedLocation(){
        _selectedLocation.postValue(null)
    }

    companion object {
        private const val INITIAL_CAMERA_LATITUDE = 35.8905341232321
        private const val INITIAL_CAMERA_LONGITUDE = 128.61213266480294
        private const val INITIAL_CAMERA_ZOOM_LEVEL = 15
        private const val INITIAL_CAMERA_TILT_ANGLE = 0.0
        private const val INITIAL_CAMERA_ROTATION_ANGLE = 0.0
        private const val INITIAL_CAMERA_HEIGHT = -1.0

        private var _instance: MapViewRepository? = null
        fun getInstance(): MapViewRepository {
            if (_instance == null) {
                _instance = MapViewRepository()
            }
            return _instance as MapViewRepository
        }

        val initialCameraPosition: CameraPosition = CameraPosition.from(
            INITIAL_CAMERA_LATITUDE,
            INITIAL_CAMERA_LONGITUDE,
            INITIAL_CAMERA_ZOOM_LEVEL,
            INITIAL_CAMERA_TILT_ANGLE,
            INITIAL_CAMERA_ROTATION_ANGLE,
            INITIAL_CAMERA_HEIGHT
        )
    }
}