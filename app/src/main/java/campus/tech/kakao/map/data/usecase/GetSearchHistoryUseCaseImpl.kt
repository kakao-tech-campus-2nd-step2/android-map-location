package campus.tech.kakao.map.data.usecase

import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.GetSearchHistoryUseCase
import campus.tech.kakao.map.domain.usecase.GetSearchPlacesUseCase

class GetSearchHistoryUseCaseImpl(private val placeRepository: PlaceRepository) :
    GetSearchHistoryUseCase {
    override fun invoke(): List<String> {
        return placeRepository.getSearchHistory()
    }
}