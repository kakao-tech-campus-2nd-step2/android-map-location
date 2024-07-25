package campus.tech.kakao

import campus.tech.kakao.Model.KakaoApiService
import campus.tech.kakao.Model.Place
import campus.tech.kakao.Model.ResultSearchKeyword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRepository(private val apiService: KakaoApiService) {

    fun searchPlaces(apiKey: String, query: String, callback: (List<Place>?, Throwable?) -> Unit) {
        apiService.searchPlaces(apiKey, query).enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                if (response.isSuccessful && response.body() != null) {
                    callback(response.body()?.documents ?: emptyList(), null)
                } else {
                    callback(null, Throwable("No results"))
                }
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}