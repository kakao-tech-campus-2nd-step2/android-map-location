package ksc.campus.tech.kakao.map.models.repositoriesImpl

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.camera.CameraPosition
import kotlinx.serialization.Serializable
import ksc.campus.tech.kakao.map.models.datasources.MapPreferenceLocalDataSource
import ksc.campus.tech.kakao.map.models.repositories.LocationInfo
import ksc.campus.tech.kakao.map.models.repositories.MapViewRepository


class MapViewRepositoryImpl(): MapViewRepository {
    private val _selectedLocation: MutableLiveData<LocationInfo?> = MutableLiveData<LocationInfo?>(null)
    private val _cameraPosition: MutableLiveData<CameraPosition> = MutableLiveData(initialCameraPosition)

    private val mapPreferenceDataSource: MapPreferenceLocalDataSource = MapPreferenceLocalDataSource()
    override val selectedLocation: LiveData<LocationInfo?>
        get() = _selectedLocation

    override val cameraPosition: LiveData<CameraPosition>
        get() = _cameraPosition

    private fun getZoomCameraPosition(latitude: Double, longitude: Double) = CameraPosition.from(
        latitude,
        longitude,
        ZOOMED_CAMERA_ZOOM_LEVEL,
        ZOOMED_CAMERA_TILT_ANGLE, ZOOMED_CAMERA_ROTATION_ANGLE,
        ZOOMED_CAMERA_HEIGHT)

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

    override fun loadFromSharedPreference(context:Context){
        val cameraPosition = loadSavedCurrentPosition(context)
        val selectedLocation = loadSavedSelectedLocation(context)

        updateCameraPosition(context, cameraPosition)
        if(selectedLocation != null)
            updateSelectedLocation(context, selectedLocation)
    }

    override fun updateSelectedLocation(context:Context, locationInfo: LocationInfo){
        saveSelectedLocation(context, locationInfo)
        _selectedLocation.postValue(locationInfo)
    }

    override fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double){
        _cameraPosition.postValue(getZoomCameraPosition(latitude, longitude))
    }

    override fun updateCameraPosition(context:Context, position: CameraPosition){
        saveCurrentPositionToSharedPreference(context, position)
        _cameraPosition.postValue(position)
    }

    override fun clearSelectedLocation(){
        _selectedLocation.postValue(null)
    }

    companion object {
        private const val ZOOMED_CAMERA_ZOOM_LEVEL = 18
        private const val ZOOMED_CAMERA_TILT_ANGLE = 0.0
        private const val ZOOMED_CAMERA_ROTATION_ANGLE = 0.0
        private const val ZOOMED_CAMERA_HEIGHT = -1.0

        val initialCameraPosition: CameraPosition = CameraPosition.from(
            35.8905341232321,
            128.61213266480294,
            15,
            0.0,
            0.0,
            -1.0
        )
    }
}