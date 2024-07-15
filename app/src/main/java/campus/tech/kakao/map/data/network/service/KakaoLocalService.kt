package campus.tech.kakao.map.data.network.service

import campus.tech.kakao.map.data.network.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoLocalService {
    @GET(URL)
    suspend fun getPlacesByCategory(
        @Query("category_group_code") categoryGroupCode: String,
        @Query("page") page: Int,
    ): PlaceResponse

    companion object {
        const val URL = "/v2/local/search/category.json"
    }
}
