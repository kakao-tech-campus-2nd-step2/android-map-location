package campus.tech.kakao.map.url

import campus.tech.kakao.map.dto.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
	@GET("keyword.json")
	fun requestPlaces(
		@Header("Authorization") authorization: String,
		@Query("query") query: String
	): Call<PlaceResponse>
}