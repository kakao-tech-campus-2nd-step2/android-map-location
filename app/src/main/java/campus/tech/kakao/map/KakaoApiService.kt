package campus.tech.kakao.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApiService {
    @GET("v2/local/search/category")
    fun getPlace(
        @Header("Authorization") apiKey: String,
        @Query("category_group_code") categoryGroupCode: String,
    ): Call<KakaoResponse>
}