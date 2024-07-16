package campus.tech.kakao.map.data.net

import campus.tech.kakao.map.domain.model.ResultSearchKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPI {
    @GET("v2/local/search/keyword.json")
    fun getSearchKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("size") size: Int = 15,
        @Query("page") page: Int = 1
    ): Call<ResultSearchKeyword>
}