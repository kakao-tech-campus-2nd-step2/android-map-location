package campus.tech.kakao.map.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastPlaceUseCase
import java.io.Serializable

class MapViewModel(
    private val saveLastPlaceUseCase: SaveLastPlaceUseCase,
    private val getLastPlaceUseCase: GetLastPlaceUseCase
) : ViewModel() {

    private val _lastPlace = MutableLiveData<PlaceVO?>()
    val lastPlace: MutableLiveData<PlaceVO?> get() = _lastPlace


    init {
        getLastPlace()
    }


    fun saveLastPlace(place: PlaceVO) {
        saveLastPlaceUseCase(place)
        _lastPlace.postValue(place)
    }

    fun getLastPlace() {
        _lastPlace.value = getLastPlaceUseCase()
    }

    fun setLastPlace(place: Serializable) {
        _lastPlace.value = place as PlaceVO
    }
}