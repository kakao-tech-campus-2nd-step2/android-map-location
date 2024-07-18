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
        currentSearches.remove(search)
        currentSearches.add(0, search)
        _savedSearches.postValue(currentSearches)
        saveSearchesToPreferences(currentSearches)
    }

    fun removeSearch(search: String) {
        val currentSearches = _savedSearches.value?.toMutableList() ?: mutableListOf()
        currentSearches.remove(search)
        _savedSearches.postValue(currentSearches)
        saveSearchesToPreferences(currentSearches)
    }

    private fun loadSavedSearches() {
        val searches = mutableListOf<String>()
        val size = sharedPreferences.getInt("search_size", 0)
        for (i in 0 until size) {
            val search = sharedPreferences.getString("search_$i", null)
            if (search != null) {
                searches.add(search)
            }
        }
        _savedSearches.postValue(searches)
    }

    private fun saveSearchesToPreferences(searches: List<String>) {
        val editor = sharedPreferences.edit()
        editor.putInt("search_size", searches.size)
        searches.forEachIndexed { index, search ->
            editor.putString("search_$index", search)
        }
        editor.apply()
    }

    fun searchSavedPlace(savedQuery: String) {
        searchPlaces(savedQuery)
    }
}