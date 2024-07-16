package ksc.campus.tech.kakao.map.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.camera.CameraPosition

data class LocationInfo(val address:String, val name:String, val latitude:Double, val longitude:Double)

class MapViewRepository() {
    private val _selectedLocation: MutableLiveData<LocationInfo?> = MutableLiveData<LocationInfo?>(null)
    private val _cameraPosition: MutableLiveData<CameraPosition> = MutableLiveData(initialCameraPosition)

    val selectedLocation: LiveData<LocationInfo?>
        get() = _selectedLocation

    val cameraPosition: LiveData<CameraPosition>
        get() = _cameraPosition

    private fun getZoomCameraPosition(latitude: Double, longitude: Double) = CameraPosition.from(latitude, longitude, 18, 0.0,0.0, -1.0)

    private fun saveCurrentPositionToSharedPreference(context:Context, position: CameraPosition){
        val sharedPreference = context.getSharedPreferences(CURRENT_POSITION_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putInt(CURRENT_POSITION_KEY_ZOOM, position.zoomLevel)
        editor.putFloat(CURRENT_POSITION_KEY_TILT,position.tiltAngle.toFloat())
        editor.putFloat(CURRENT_POSITION_KEY_ROTATION,position.rotationAngle.toFloat())
        editor.putFloat(CURRENT_POSITION_KEY_HEIGHT,position.height.toFloat())
        editor.putFloat(CURRENT_POSITION_KEY_LATITUDE, position.position.latitude.toFloat())
        editor.putFloat(CURRENT_POSITION_KEY_LONGITUDE, position.position.longitude.toFloat())
        editor.apply()
    }

    private fun saveSelectedLocation(context:Context, location: LocationInfo){
        val sharedPreference = context.getSharedPreferences(SELECTED_LOCATION_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(SELECTED_LOCATION_KEY_NAME, location.name)
        editor.putString(SELECTED_LOCATION_KEY_ADDRESS, location.address)
        editor.putFloat(SELECTED_LOCATION_KEY_LATITUDE, location.latitude.toFloat())
        editor.putFloat(SELECTED_LOCATION_KEY_LONGITUDE, location.longitude.toFloat())
        editor.apply()
    }

    private fun loadSavedCurrentPosition(context:Context): CameraPosition {
        val sharedPreference = context.getSharedPreferences(CURRENT_POSITION_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return CameraPosition.from(
            sharedPreference.getFloat(CURRENT_POSITION_KEY_LATITUDE, initialCameraPosition.position.latitude.toFloat()).toDouble(),
            sharedPreference.getFloat(CURRENT_POSITION_KEY_LONGITUDE, initialCameraPosition.position.longitude.toFloat()).toDouble(),
            sharedPreference.getInt(CURRENT_POSITION_KEY_ZOOM, initialCameraPosition.zoomLevel),
            sharedPreference.getFloat(CURRENT_POSITION_KEY_TILT, initialCameraPosition.tiltAngle.toFloat()).toDouble(),
            sharedPreference.getFloat(CURRENT_POSITION_KEY_ROTATION, initialCameraPosition.rotationAngle.toFloat()).toDouble(),
            sharedPreference.getFloat(CURRENT_POSITION_KEY_HEIGHT, initialCameraPosition.height.toFloat()).toDouble()
        )
    }

    private fun loadSavedSelectedLocation(context:Context): LocationInfo? {
        val sharedPreferences =
            context.getSharedPreferences(SELECTED_LOCATION_PREFERENCE_NAME, Context.MODE_PRIVATE)

        if (!sharedPreferences.contains(SELECTED_LOCATION_KEY_ADDRESS)) {
            return null
        }
        return LocationInfo(
            sharedPreferences.getString(SELECTED_LOCATION_KEY_ADDRESS, "") ?: "",
            sharedPreferences.getString(SELECTED_LOCATION_KEY_NAME, "") ?: "",
            sharedPreferences.getFloat(SELECTED_LOCATION_KEY_LATITUDE, 0.0f).toDouble(),
            sharedPreferences.getFloat(SELECTED_LOCATION_KEY_LONGITUDE, 0.0f).toDouble()
        )
    }

    fun loadFromSharedPreference(context:Context){
        val cameraPosition = loadSavedCurrentPosition(context)
        val selectedLocation = loadSavedSelectedLocation(context)

        updateCameraPosition(context, cameraPosition)
        if(selectedLocation != null)
            updateSelectedLocation(context, selectedLocation)
    }

    fun updateSelectedLocation(context:Context, locationInfo: LocationInfo){
        _selectedLocation.postValue(locationInfo)
        saveSelectedLocation(context, locationInfo)
    }

    fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double){
        _cameraPosition.postValue(getZoomCameraPosition(latitude, longitude))
    }

    fun updateCameraPosition(context:Context, position: CameraPosition){
        _cameraPosition.postValue(position)
        saveCurrentPositionToSharedPreference(context, position)
    }

    fun clearSelectedLocation(){
        _selectedLocation.postValue(null)
    }

    companion object {
        private const val CURRENT_POSITION_PREFERENCE_NAME = "currentPosition"
        private const val CURRENT_POSITION_KEY_ZOOM = "zoom"
        private const val CURRENT_POSITION_KEY_TILT = "tilt"
        private const val CURRENT_POSITION_KEY_ROTATION = "rotation"
        private const val CURRENT_POSITION_KEY_HEIGHT = "height"
        private const val CURRENT_POSITION_KEY_LATITUDE = "latitude"
        private const val CURRENT_POSITION_KEY_LONGITUDE = "longitude"

        private const val SELECTED_LOCATION_PREFERENCE_NAME = "selectedLocation"
        private const val SELECTED_LOCATION_KEY_NAME = "name"
        private const val SELECTED_LOCATION_KEY_ADDRESS = "address"
        private const val SELECTED_LOCATION_KEY_LATITUDE = "latitude"
        private const val SELECTED_LOCATION_KEY_LONGITUDE = "longitude"

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