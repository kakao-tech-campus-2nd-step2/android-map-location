package campus.tech.kakao.map.data.net

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.domain.model.ResultSearchKeyword
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApi {
    @GET("v2/local/search/keyword.json")
    suspend fun getSearchKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("size") size: Int = 15,
        @Query("page") page: Int = 1
    ): Response<ResultSearchKeyword>
}