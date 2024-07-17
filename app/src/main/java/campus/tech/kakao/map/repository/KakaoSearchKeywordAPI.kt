package campus.tech.kakao.map.repository

import campus.tech.kakao.map.model.SearchResults
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoSearchKeywordAPI {
    @GET("/v2/local/search/keyword.json")
    suspend fun getSearchKeyWord(
        @Header("Authorization") key: String,
        @Query("query") keyword: String
    ): SearchResults
}