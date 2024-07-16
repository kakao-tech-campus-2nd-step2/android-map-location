package campus.tech.kakao.map.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.Domain.PlaceRepository

class SearchViewModel(private val repository: PlaceRepository) : ViewModel() {

    private val _currentResult: MutableLiveData<List<Place>> = MutableLiveData()
    val currentResult: LiveData<List<Place>> = _currentResult
    private val _favoritePlace: MutableLiveData<List<Place>> = MutableLiveData()
    val favoritePlace: LiveData<List<Place>> = _favoritePlace

    init {
        _currentResult.value = listOf<Place>()
        _favoritePlace.value = repository.getCurrentFavorite()
    }

    fun searchPlace(string: String) {
        _currentResult.value = repository.getSimilarPlacesByName(string)
    }

    suspend fun searchPlaceRemote(name: String) {
        _currentResult.postValue(repository.searchPlaceRemote(name))
    }

    fun addFavorite(id : Int) {
        val place = _currentResult.value?.find {
            it.id == id
        }

        if (isPlaceInFavorite(id)) return

        place?.let {
            repository.addFavorite(it).run {
                _favoritePlace.value = this
            }
        }
    }

    fun deleteFromFavorite(id : Int) {
        repository.deleteFavorite(id).run {
            _favoritePlace.value = this
        }
    }

    private fun isPlaceInFavorite(id : Int): Boolean {
        return (favoritePlace.value?.find { it.id == id }) != null
    }


}