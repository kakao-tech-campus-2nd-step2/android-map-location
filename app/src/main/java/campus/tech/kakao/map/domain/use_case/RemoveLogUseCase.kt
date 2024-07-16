package campus.tech.kakao.map.domain.use_case

import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class RemoveLogUseCase(
    private val placeRepository: PlaceRepository
){
    operator fun invoke(id: String) {
        placeRepository.removeLog(id)
    }
}