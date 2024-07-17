package campus.tech.kakao.map.network

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.kakaolocal.KakaoLocalResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocalService {
    @GET("/v2/local/search/keyword.json")
    fun searchKeyword(
        @Header("Authorization") authorization: String = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
//        @Query("x") x: String = "37.514322572335935",
//        @Query("y") y: String = "127.06283102249932",
        @Query("size") size: String = "15",
        @Query("query") query: String,
    ): Call<KakaoLocalResponse>
}
