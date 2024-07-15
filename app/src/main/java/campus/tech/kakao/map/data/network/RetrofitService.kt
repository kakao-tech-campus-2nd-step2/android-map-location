package campus.tech.kakao.map.data.network

import campus.tech.kakao.map.data.model.SearchResponse
import campus.tech.kakao.map.utils.ApiKeyProvider
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("v2/local/search/keyword.json")
    fun searchKeyword(
        @Header("Authorization") apiKey: String = ApiKeyProvider.KAKAO_REST_API_KEY,
        @Query("query") query: String,
    ): Call<SearchResponse>

}