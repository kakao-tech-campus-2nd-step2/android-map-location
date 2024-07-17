package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.SavedPlaceRepository


class MainActivityViewModel(
    private val placeRepository: PlaceRepository,
    private val savedPlaceRepository: SavedPlaceRepository
) : ViewModel() {
    private val _place = MutableLiveData<List<Place>>()
    private val _savedPlace = MutableLiveData<List<SavedPlace>>()
    val place: LiveData<List<Place>> get() = _place
    val savedPlace: LiveData<List<SavedPlace>> get() = _savedPlace

    init {
        getSavedPlace()
        getPlaceWithCategory("")
    }

    fun getPlace() {
        _place.value = (placeRepository.getAllPlace())
    }

    fun getPlaceWithCategory(category: String) {
        _place.value = (placeRepository.getPlaceWithCategory(category))
    }

    fun getSavedPlace() {
        _savedPlace.value = (savedPlaceRepository.getAllSavedPlace())
    }

    fun savePlace(place: Place) {
        savedPlaceRepository.writePlace(place)
        getSavedPlace()
    }

    fun deleteSavedPlace(savedPlace: SavedPlace) {
        savedPlaceRepository.deleteSavedPlace(savedPlace)
        getSavedPlace()
    }


    suspend fun getKakaoLocalData(text: String) {
        Log.d("coroutineTest", "getKakaoLocalData")
        if (text.isNotEmpty()) {
            val placeList = placeRepository.getKakaoLocalPlaceData(text)
            Log.d("coroutineTest", "getKakaoLocalDataReturn")
            _place.value = (placeList)
        } else _place.value = listOf<Place>()
    }
}