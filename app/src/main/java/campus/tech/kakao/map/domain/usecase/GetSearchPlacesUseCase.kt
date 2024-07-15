package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.PlaceVO

interface GetSearchPlacesUseCase {
    operator fun invoke(query: String, callback: (List<PlaceVO>?) -> Unit)
}