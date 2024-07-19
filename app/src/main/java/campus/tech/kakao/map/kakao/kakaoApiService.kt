package campus.tech.kakao.map.kakao

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApiService {
    @GET("https://dapi.kakao.com/v2/local/search/keyword.json?query=your_query_here")
    suspend fun searchAddress(
        @Query("query") query: String
    ): KakaoSearchResponse
}