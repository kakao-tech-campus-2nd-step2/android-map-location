package campus.tech.kakao.map.network

import campus.tech.kakao.map.model.KakaoMapProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoMapRetrofitService {
    @GET("v2/local/search/keyword.json")
    fun searchPlaces(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String
    ): Call<KakaoMapProductResponse>
}