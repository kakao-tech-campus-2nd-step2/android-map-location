package campus.tech.kakao.map.domain

import campus.tech.kakao.map.data.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitLocalKeywordService {
    @GET("/v2/local/search/keyword.json")
    fun searchPlaceByKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String
    ): Call<SearchResult>
}