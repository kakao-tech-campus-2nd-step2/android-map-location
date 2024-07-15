package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.PlaceVO

interface SaveSearchQueryUseCase {
    operator fun invoke(place: PlaceVO)
}