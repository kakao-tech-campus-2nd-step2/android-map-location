package campus.tech.kakao.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import campus.tech.kakao.map.ui.search.PlaceViewModel
import campus.tech.kakao.map.ui.search.SavedSearchWordViewModel

class ViewModelFactory(
    private val placeRepository: PlaceRepository,
    private val savedSearchWordRepository: SavedSearchWordRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaceViewModel(placeRepository) as T
        } else if (modelClass.isAssignableFrom(SavedSearchWordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SavedSearchWordViewModel(savedSearchWordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
