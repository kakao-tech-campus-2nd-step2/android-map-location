package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.model.Place

interface PlaceRepository {
    suspend fun getPlacesByCategory(
        categoryInput: String,
        totalPageCount: Int,
    ): List<Place>
}
