package campus.tech.kakao.map.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocalService {
    @GET("v2/local/search/keyword.json")
    fun getPlaceList(
        @Header("Authorization") key: String,
        @Query("query") categoryGroupName: String
    ): Call<SearchPlace>
}
