package campus.tech.kakao.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoLocalApiService {
    @GET("v2/local/search/keyword.json")
    fun searchPlaces(
        @Query("query") query: String,
        @Query("size") size: Int = 15
    ): Call<KakaoSearchResponse>
}
