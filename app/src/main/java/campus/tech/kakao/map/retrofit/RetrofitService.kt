package campus.tech.kakao.map.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("keyword.json")
    fun requestResults(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String
    ): Call<MapSearchResponse>
}
