package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.PlaceVO

interface GetLastPlaceUseCase {
    operator fun invoke(): PlaceVO?
}