package campus.tech.kakao.map.presentation

import campus.tech.kakao.map.domain.usecase.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.domain.model.PlaceVO


class PlaceViewModel(
    private val getSearchPlacesUseCase: GetSearchPlacesUseCase,
    private val saveSearchQueryUseCase: SaveSearchQueryUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val removeSearchQueryUseCase: RemoveSearchQueryUseCase
) : ViewModel() {
    private val _places = MutableLiveData<List<PlaceVO>>()
    val places: LiveData<List<PlaceVO>> get() = _places
    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: LiveData<List<String>> get() = _searchHistory

    fun searchPlaces(query: String) {
        getSearchPlacesUseCase(query) {
            _places.postValue(it)
        }
    }

    fun saveSearchQuery(place: PlaceVO) {
        saveSearchQueryUseCase(place)
        loadSearchHistory()
    }

    fun loadSearchHistory() {
        val history = getSearchHistoryUseCase()
        _searchHistory.postValue(history.toList())
    }

    fun removeSearchQuery(query: String) {
        removeSearchQueryUseCase(query)
        loadSearchHistory()
    }

}