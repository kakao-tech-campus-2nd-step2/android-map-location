package campus.tech.kakao.map.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.search.SavedSearchKeywordRepository
import campus.tech.kakao.map.repository.search.SearchRepository

class SearchViewModelFactory(
    private val searchRepository: SearchRepository,
    private val savedSearchKeywordRepository: SavedSearchKeywordRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchRepository, savedSearchKeywordRepository) as T
    }
}