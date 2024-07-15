package campus.tech.kakao.map.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.RecentSearchWord

class PlacesViewModel(private val repository: MapRepository) : ViewModel() {

    val places: LiveData<List<Place>> = repository.places

    private val _searchHistoryData: MutableLiveData<ArrayList<RecentSearchWord>> =
        MutableLiveData<ArrayList<RecentSearchWord>>()
    val searchHistoryData: LiveData<ArrayList<RecentSearchWord>> = _searchHistoryData

    init {
        _searchHistoryData.value = repository.searchHistoryList
    }

    fun searchPlaces(search: String) {
        repository.searchPlaces(search)
    }

    fun searchDBPlaces(search: String) {
        repository.searchDBPlaces(search)
    }

    fun getSearchHistory(): List<RecentSearchWord> {
        return searchHistoryData.value ?: emptyList()
    }

    fun moveSearchToLast(idx: Int, search: String) {
        repository.moveSearchToLast(idx, search)
        _searchHistoryData.value = repository.searchHistoryList
    }

    fun addSearch(search: String) {
        repository.addSearchHistory(search)
        _searchHistoryData.value = repository.searchHistoryList
    }

    fun delSearch(idx: Int) {
        repository.delSearchHistory(idx)
        _searchHistoryData.value = repository.searchHistoryList
    }
}