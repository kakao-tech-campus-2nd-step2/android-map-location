package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = KakaoAPIRetrofitClient.retrofitService
    private val repository = PlaceRepository(apiService)
    private val preferencesRepository = PreferencesRepository(application.applicationContext)

    private val _searchResults = MutableLiveData<List<Document>>()
    val searchResults: LiveData<List<Document>> get() = _searchResults

    private val _savedSearches = MutableLiveData<List<String>>()
    val savedSearches: LiveData<List<String>> get() = _savedSearches

    init {
        loadSavedSearches()
    }

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            val results = repository.searchPlaces(query)
            _searchResults.postValue(results)
        }
    }

    fun addSearch(search: String) {
        val old = _savedSearches.value ?: emptyList()
        val new = listOf(search) + (old - setOf(search))
        _savedSearches.postValue(new)
        saveSearchesToPreferences(new)
    }

    fun removeSearch(search: String) {
        val old = _savedSearches.value ?: emptyList()
        val new = old - search
        _savedSearches.postValue(new)
        saveSearchesToPreferences(new)
    }

    private fun loadSavedSearches() {
        val searches = preferencesRepository.getSavedSearches()
        _savedSearches.postValue(searches)
    }

    private fun saveSearchesToPreferences(searches: List<String>) {
        preferencesRepository.saveSearches(searches)
    }

    fun searchSavedPlace(savedQuery: String) {
        searchPlaces(savedQuery)
    }
}