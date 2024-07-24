package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.SavedPlaceRepository
import campus.tech.kakao.map.repository.SharedPreferenceRepository
import com.kakao.vectormap.LatLng


class MapActivityViewModel(
    private val sharedPreferenceRepository: SharedPreferenceRepository
) : ViewModel() {
    private val _recentPos = MutableLiveData<LatLng>()
    val recentPos : LiveData<LatLng> get() = _recentPos

    init {
        getRecentPos()
    }

    fun getRecentPos(){
        _recentPos.value = sharedPreferenceRepository.getPos()
    }

    fun setRecentPos(latitude : Double, longitude : Double){
        sharedPreferenceRepository.putPos(latitude, longitude)
    }
}