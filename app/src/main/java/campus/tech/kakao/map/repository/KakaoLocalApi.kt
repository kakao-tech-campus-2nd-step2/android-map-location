package campus.tech.kakao.map.repository

import campus.tech.kakao.map.model.ResultSearch
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocalApi {
    @GET("v2/local/search/keyword.json")
    suspend fun getPlaceData(
        @Query("query") query: String
    ): ResultSearch

}