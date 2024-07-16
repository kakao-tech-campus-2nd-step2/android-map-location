package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.SavedSearchKeywordRepository
import campus.tech.kakao.map.repository.SearchRepository

class SearchViewModelFactory(
    private val searchRepository: SearchRepository,
    private val savedSearchKeywordRepository: SavedSearchKeywordRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchRepository, savedSearchKeywordRepository) as T
    }
}