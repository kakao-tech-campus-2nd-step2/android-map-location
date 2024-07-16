package campus.tech.kakao.map.domain.use_case

import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class GetLogsUseCase(
    private val placeRepository: PlaceRepository
){
    operator fun invoke(): List<Place> {
        return placeRepository.getLogs()
    }
}