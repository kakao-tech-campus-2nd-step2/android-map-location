package campus.tech.kakao.map.data.usecase

import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.GetSearchPlacesUseCase

class GetSearchPlacesUseCaseImpl(private val placeRepository: PlaceRepository) :
    GetSearchPlacesUseCase {
    override fun invoke(query: String, callback: (List<PlaceVO>?) -> Unit) {
        placeRepository.searchPlaces(query, callback)
    }

}