package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Place

interface PlaceRepository {
    fun getPlaces(placeName: String): List<Place>
    fun getAllPlaces():List<Place>
    fun updatePlaces(places:List<Place>)
    fun updateLogs(placeLog: List<Place>)
    fun removeLog(id: String)
    fun getLogs(): List<Place>

}