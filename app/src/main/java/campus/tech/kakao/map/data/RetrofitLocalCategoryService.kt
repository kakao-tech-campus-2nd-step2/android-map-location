package campus.tech.kakao.map.data

import campus.tech.kakao.map.data.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitLocalCategoryService {
    @GET("/v2/local/search/category.json")
    fun searchPlaceByCategory(
        @Header("Authorization") key: String,
        @Query("category_group_code") categoryGroupCode: String
    ): Call<SearchResult>
}