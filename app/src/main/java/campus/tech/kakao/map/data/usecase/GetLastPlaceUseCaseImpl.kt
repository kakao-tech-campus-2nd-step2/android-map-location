package campus.tech.kakao.map.data.usecase

import android.util.Log
import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase

class GetLastPlaceUseCaseImpl (private val placeRepository: PlaceRepository): GetLastPlaceUseCase {
    override fun invoke(): PlaceVO? {
        return placeRepository.getLastPlace()
    }

}