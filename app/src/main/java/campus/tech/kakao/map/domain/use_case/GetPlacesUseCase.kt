package campus.tech.kakao.map.domain.use_case

import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class GetPlacesUseCase(
    private val placeRepository: PlaceRepository
) {
    operator fun invoke(placeName: String): List<Place>{
        return placeRepository.getPlaces(placeName)
    }
}