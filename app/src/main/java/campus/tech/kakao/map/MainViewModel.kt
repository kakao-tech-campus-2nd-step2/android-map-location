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
    private val sharedPreferences = application.getSharedPreferences("search_prefs", Context.MODE_PRIVATE)

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
        val currentSearches = _savedSearches.value?.toMutableList() ?: mutableListOf()
        if (!currentSearches.contains(search)) {
            currentSearches.add(0, search)
            _savedSearches.postValue(currentSearches)
            saveSearchesToPreferences(currentSearches)
        }
    }

    fun removeSearch(search: String) {
        val currentSearches = _savedSearches.value?.toMutableList() ?: mutableListOf()
        currentSearches.remove(search)
        _savedSearches.postValue(currentSearches)
        saveSearchesToPreferences(currentSearches)
    }

    private fun loadSavedSearches() {
        val searches = sharedPreferences.getStringSet("saved_searches", emptySet())?.toList() ?: emptyList()
        _savedSearches.postValue(searches)
    }

    private fun saveSearchesToPreferences(searches: List<String>) {
        sharedPreferences.edit().putStringSet("saved_searches", searches.toSet()).apply()
    }

    fun searchSavedPlace(savedQuery: String) {
        searchPlaces(savedQuery)
    }
}