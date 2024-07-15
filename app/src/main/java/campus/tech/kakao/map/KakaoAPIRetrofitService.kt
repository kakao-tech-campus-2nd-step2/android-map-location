package campus.tech.kakao.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPIRetrofitService {
    @GET("v2/local/search/keyword.json")

    suspend fun getSearchKeyword(
        @Query("query") query: String
    ): SearchResponse
}