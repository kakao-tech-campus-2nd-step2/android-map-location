package campus.tech.kakao.map

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SearchViewModel(private val context: Context) : ViewModel() {
    private val repository: Repository = Repository(context)

    private val _searchResults = MutableLiveData<List<Keyword>>()
    val searchResults: LiveData<List<Keyword>> = _searchResults

    private val _savedKeywords = MutableLiveData<List<Keyword>>()
    val savedKeywords: LiveData<List<Keyword>> = _savedKeywords

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
