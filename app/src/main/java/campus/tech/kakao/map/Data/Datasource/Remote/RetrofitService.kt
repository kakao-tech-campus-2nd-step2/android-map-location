package campus.tech.kakao.map.Data.Datasource.Remote

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.Data.Datasource.Remote.Response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET(URL)
    fun requestProducts(
        @Header("authorization") auth: String = KEY,
        @Query("x") x : Double = X,
        @Query("y") y : Double = Y,
        @Query("radius") radius : Int = RADIUS,
        @Query("query",encoded = true) query: String = "",
        @Query("page") page: Int = CURRENT_PAGE
    ): Call<SearchResponse>

    companion object {
        const val BASE = BuildConfig.BASE_URL
        private const val KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        private const val URL = "/v2/local/search/keyword.json"
        private const val X = 127.06283102249932
        private const val Y = 37.514322572335935
        private const val RADIUS = 20000
        private const val CURRENT_PAGE = 1
    }
}