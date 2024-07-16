package campus.tech.kakao.map.domain.use_case

import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class UpdateLogsUseCase(
    private val placeRepository: PlaceRepository
){
    operator fun invoke(logs: List<Place>){
        return placeRepository.updateLogs(logs)
    }
}