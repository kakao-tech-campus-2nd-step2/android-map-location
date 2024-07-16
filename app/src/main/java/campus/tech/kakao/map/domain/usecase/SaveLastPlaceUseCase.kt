package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.PlaceVO

interface SaveLastPlaceUseCase {
    operator fun invoke(place: PlaceVO)
}