package campus.tech.kakao.map

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("/v2/local/search/keyword.json")
    fun requsetKakaoMap(
        @Header("Authorization") authorization: String = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
        @Query("query", encoded = true) query: String = "",
        @Query("x") x: String = "127.06283102249932",
        @Query("y") y: String = "37.514322572335935",
        @Query("radius") radius: Int = 20000,
        @Query("page") page: Int = 3
    ): Call<KakaoMapResponse>
}

val retrofitService = Retrofit.Builder()
    .baseUrl("https://dapi.kakao.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(RetrofitService::class.java)