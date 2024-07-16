package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.repository.SavedSearchKeywordRepository
import campus.tech.kakao.map.model.SearchKeyword
import campus.tech.kakao.map.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val savedSearchKeywordRepository: SavedSearchKeywordRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults: StateFlow<List<Place>> get() = _searchResults
    private val _savedSearchKeywords = MutableStateFlow<List<SearchKeyword>>(emptyList())
    val savedSearchKeywords: StateFlow<List<SearchKeyword>> get() = _savedSearchKeywords

    init {
        getSavedSearchKeywords()
    }

    fun getSearchResults(searchKeyword: SearchKeyword) {
        viewModelScope.launch{
            _searchResults.value = searchRepository.Search(searchKeyword)
        }
    }

    fun saveSearchKeyword(searchKeyword: SearchKeyword) {
        savedSearchKeywordRepository.saveSearchKeyword(searchKeyword)
        getSavedSearchKeywords()
    }

    fun getSavedSearchKeywords() {
        _savedSearchKeywords.value = savedSearchKeywordRepository.getSavedSearchKeywords()
    }

    fun delSavedSearchKeyword(searchKeyword: SearchKeyword) {
        savedSearchKeywordRepository.delSavedSearchKeyword(searchKeyword)
        getSavedSearchKeywords()
    }
}