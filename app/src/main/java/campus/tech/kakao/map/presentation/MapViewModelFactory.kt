package campus.tech.kakao.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastPlaceUseCase

class MapViewModelFactory(
    private val saveLastPlaceUseCase: SaveLastPlaceUseCase,
    private val getLastPlaceUseCase: GetLastPlaceUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(saveLastPlaceUseCase, getLastPlaceUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
