package campus.tech.kakao.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class MapViewModel(dbHelper: MapDbHelper) : ViewModel() {
    private val model: MapModel = MapModel(dbHelper)
    private val _searchResult = MutableLiveData<List<Location>>()
    val searchResult: LiveData<List<Location>> = _searchResult
    private val _searchHistory = MutableLiveData<List<Location>>()
    val searchHistory: LiveData<List<Location>> = _searchHistory

    private val resultObserver = Observer<List<Location>> {
        _searchResult.value = it
    }
    private val historyObserver = Observer<List<Location>> {
        _searchHistory.value = it
    }

    init {
        observeData()
//        _searchResult.value = model.getAllLocation()
//        _searchHistory.value = model.getAllHistory()
    }

    override fun onCleared() {
        super.onCleared()
        model.searchResult.removeObserver(resultObserver)
        model.searchHistory.removeObserver(historyObserver)
    }

    fun insertLocation(location: Location) {
        model.insertLocation(location)
    }

    fun searchLocation(locName: String, isExactMatch: Boolean) {
        _searchResult.value = model.getSearchedLocation(locName, isExactMatch)
    }

    fun getAllLocation(): List<Location> {
        return model.getAllLocation()
    }

    fun deleteHistory(oldHistory: Location) {
        model.deleteHistory(oldHistory)
        _searchHistory.value = model.getAllHistory()
    }

    fun insertHistory(newHistory: Location) {
        model.insertHistory(newHistory)
        _searchHistory.value = model.getAllHistory()
    }

    fun getAllHistory(): List<Location> {
        return model.getAllHistory()
    }

    fun searchByKeywordFromServer(keyword: String, isExactMatch: Boolean) {
        model.searchByKeywordFromServer(keyword, isExactMatch)
//        _searchResult.value = model.getAllLocation()
//        Log.d("ViewModel", model.getAllLocation().toString())
    }

    private fun observeData() {
        model.searchResult.observeForever(resultObserver)
        model.searchHistory.observeForever(historyObserver)
    }
}