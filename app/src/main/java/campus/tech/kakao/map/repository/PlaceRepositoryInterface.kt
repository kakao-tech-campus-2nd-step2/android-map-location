package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.db.entity.Place

interface PlaceRepositoryInterface {
    fun searchPlaces(query: String, callback: (List<Place>) -> Unit)
    fun saveLastLocation(item: Place)
}