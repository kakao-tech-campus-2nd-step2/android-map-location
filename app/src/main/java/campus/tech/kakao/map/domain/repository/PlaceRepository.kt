package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Place

interface PlaceRepository {
    suspend fun getPlaces(placeName: String): List<Place>
    suspend fun updatePlaces(places:List<Place>)
    fun getLogs(): List<Place>
    fun updateLogs(placeLog: List<Place>)
    fun removeLog(id: String)

}