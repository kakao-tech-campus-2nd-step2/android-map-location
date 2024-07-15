package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.PlaceVO

interface PlaceRepository {
    fun searchPlaces(query: String, callback: (List<PlaceVO>?) -> Unit)
    fun saveSearchQuery(place: PlaceVO)
    fun getSearchHistory(): List<String>
    fun removeSearchQuery(query: String)
}
