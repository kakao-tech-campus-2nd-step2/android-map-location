package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.SavedPlaceRepository

class ViewModelFactory(
    private val placeRepository: PlaceRepository,
    private val savedPlaceRepository: SavedPlaceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(placeRepository, savedPlaceRepository) as T
        }
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}