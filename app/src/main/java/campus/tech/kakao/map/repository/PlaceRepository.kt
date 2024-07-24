package campus.tech.kakao.map.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.base.MyApplication
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.data.remote.model.KakaoResponse
import campus.tech.kakao.map.data.remote.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRepository(private val application: MyApplication): PlaceRepositoryInterface {
    private val sharedPreferences = application.getSharedPreferences("LastLocation", Context.MODE_PRIVATE)

    override fun searchPlaces(query: String, callback: (List<Place>) -> Unit){
        val apiKey = "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY
        val retrofitService = RetrofitClient.retrofitService

        retrofitService.getPlace(apiKey, query)
            .enqueue(object : Callback<KakaoResponse> {
                override fun onResponse(
                    call: Call<KakaoResponse>,
                    response: Response<KakaoResponse>
                ) {
                    if (response.isSuccessful) {
                        val documentList = response.body()?.documents ?: emptyList()
                        val placeList = documentList.map {
                            Place(
                                img = R.drawable.location,
                                name = it.placeName,
                                location = it.addressName,
                                category = it.categoryGroupName,
                                x = it.x,
                                y = it.y)
                        }
                        callback(placeList)
                    } else {
                        Log.d("KakaoAPI", response.errorBody()?.string().toString())
                        callback(emptyList())
                    }
                }

                override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                    Log.d("KakaoAPI", "Failure: ${t.message}")
                    callback(emptyList())
                }
            })
    }

    override fun saveLastLocation(item: Place) {
        with(sharedPreferences.edit()) {
            putString("PLACE_X", item.x)
            putString("PLACE_Y", item.y)
            apply()
        }
    }

}