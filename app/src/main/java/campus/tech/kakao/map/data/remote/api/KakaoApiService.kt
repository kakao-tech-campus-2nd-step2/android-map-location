package campus.tech.kakao.map.data.remote.api

import campus.tech.kakao.map.data.remote.model.KakaoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApiService {
    @GET("v2/local/search/keyword")
    fun getPlace(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
    ): Call<KakaoResponse>
}