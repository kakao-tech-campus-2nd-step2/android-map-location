package campus.tech.kakao.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(
    private val searchRepository: SearchRepository,
    private val savedSearchKeywordRepository: SavedSearchKeywordRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchRepository, savedSearchKeywordRepository) as T
    }
}