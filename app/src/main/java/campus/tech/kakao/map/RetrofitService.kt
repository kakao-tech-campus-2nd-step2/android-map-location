package campus.tech.kakao.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("v2/local/search/category")
    fun requestLocationByCategory(
        @Header("Authorization") authorization: String,
        @Query("category_group_code") categoryGroupCode: String,
        @Query("rect") rect: String = "20000",
        @Query("page") page: Int = 1
    ) : Call<ServerResult>

    @GET("v2/local/search/keyword")
    fun requestLocationByKeyword(
        @Header("Authorization") authorization: String,
        @Query("query") keyword: String,
        @Query("page") page: Int = 1
    ) : Call<ServerResult>
}