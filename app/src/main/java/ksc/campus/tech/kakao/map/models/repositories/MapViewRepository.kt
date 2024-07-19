package ksc.campus.tech.kakao.map.models.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.kakao.vectormap.camera.CameraPosition
import kotlin.reflect.KType

data class LocationInfo(val address:String, val name:String, val latitude:Double, val longitude:Double)

interface MapViewRepository {
    val selectedLocation: LiveData<LocationInfo?>
    val cameraPosition: LiveData<CameraPosition>

    fun loadFromSharedPreference(context: Context)
    fun updateSelectedLocation(context:Context, locationInfo: LocationInfo)
    fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double)
    fun updateCameraPosition(context:Context, position: CameraPosition)
    fun clearSelectedLocation()

    companion object{
        private var _instance: MapViewRepository? = null

        fun injectDependency(repository: MapViewRepository){
            if (_instance == null) {
                _instance = repository
            }
        }

        fun getInstance(): MapViewRepository{
            if(_instance == null){
                Log.e("KSC","instance is null")
                throw NullPointerException()
            }
            return _instance!!
        }
    }
}