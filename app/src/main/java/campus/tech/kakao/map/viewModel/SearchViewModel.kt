package campus.tech.kakao.map.viewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import campus.tech.kakao.map.data.Keyword
import campus.tech.kakao.map.repository.Repository
import kotlinx.coroutines.launch

class SearchViewModel(private val context: Context) : ViewModel() {
    private val repository: Repository = Repository(context)

    private val _searchResults = MutableLiveData<List<Keyword>>()
    val searchResults: LiveData<List<Keyword>> = _searchResults

    private val _savedKeywords = MutableLiveData<List<Keyword>>()
    val savedKeywords: LiveData<List<Keyword>> = _savedKeywords

    private val _selectedKeyword = MutableLiveData<Keyword>()
    val selectedKeyword: LiveData<Keyword> = _selectedKeyword

    init {
        _savedKeywords.value = repository.getAllSavedKeywordsFromPrefs()
    }

    fun search(query: String) {
        viewModelScope.launch {
            val results = repository.search(query)
            _searchResults.value = results
        }
    }

    fun saveKeyword(keyword: Keyword) {
        val currentSavedKeywords = _savedKeywords.value?.toMutableList() ?: mutableListOf()
        if (!currentSavedKeywords.contains(keyword)) {
            currentSavedKeywords.add(0, keyword) // 최신 키워드를 앞에 추가
            _savedKeywords.value = currentSavedKeywords
            repository.saveKeywordToPrefs(keyword)
        }
    }

    fun deleteKeyword(keyword: Keyword) {
        val currentSavedKeywords = _savedKeywords.value?.toMutableList() ?: mutableListOf()
        currentSavedKeywords.remove(keyword)
        _savedKeywords.value = currentSavedKeywords
        repository.deleteKeywordFromPrefs(keyword)
    }

    fun processActivityResult(data: Intent) {
        val placeName = data.getStringExtra("place_name")
        val roadAddressName = data.getStringExtra("road_address_name")
        val x = data.getDoubleExtra("x", 0.0)
        val y = data.getDoubleExtra("y", 0.0)
        if (placeName != null && roadAddressName != null) {
            _selectedKeyword.value = Keyword(0, placeName, roadAddressName, x, y)
        }
    }
}

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
