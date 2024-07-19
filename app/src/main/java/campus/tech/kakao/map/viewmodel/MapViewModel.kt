package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.model.PlaceInfo
import campus.tech.kakao.map.repository.MapRepository
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val mapRepo: MapRepository = MapRepository(application)
    private val _kakaoMap = MutableLiveData<KakaoMap>()
    val kakaoMap: LiveData<KakaoMap> get() = _kakaoMap

    private val _pos = MutableLiveData<LatLng>()
    val pos: LiveData<LatLng> get() = _pos

    private val _clickedPlaceInfo = MutableLiveData<PlaceInfo>()
    val clickedPlaceInfo: LiveData<PlaceInfo> get() = _clickedPlaceInfo

    fun setKakaoMap(map: KakaoMap) {
        _kakaoMap.value = map
    }

    fun setClickedPlaceInfo(placeInfo: PlaceInfo) {
        _clickedPlaceInfo.value = placeInfo
        _pos.value = LatLng.from(placeInfo.y.toDouble(), placeInfo.x.toDouble())
    }

    fun saveLastPosition() {
        _kakaoMap.value?.cameraPosition?.let { camera ->
            mapRepo.saveLastPosition(camera.position.latitude, camera.position.longitude)
        }
    }

    fun getLastPosition() {
        val (latitude, longitude) = mapRepo.getLastPosition()
        _pos.value = LatLng.from(latitude, longitude)
    }
}
