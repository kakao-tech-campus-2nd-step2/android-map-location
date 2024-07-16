package campus.tech.kakao.map.presentation

import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastPlaceUseCase

class MapViewModel(
    private val saveLastPlaceUseCase: SaveLastPlaceUseCase,
    private val getLastPlaceUseCase: GetLastPlaceUseCase
) : ViewModel() {

    fun saveLastPlace(place: PlaceVO) {
        saveLastPlaceUseCase(place)
    }

    fun getLastPlace(): PlaceVO? {
        return getLastPlaceUseCase()
    }
}