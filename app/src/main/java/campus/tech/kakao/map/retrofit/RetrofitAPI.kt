package campus.tech.kakao.map.retrofit

import android.util.Log
import campus.tech.kakao.map.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPI {
    //실제 사용될 때 Retrofit 객체 생성
     val retrofitService: RetrofitService by lazy {
        Log.d("yeong", "Use Retrofit!")
        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/v2/local/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }

    //카카오맵 API 검색 결과
    fun getResultFromAPI(keyword: String, callback: (Response<MapSearchResponse>) -> Unit) {
        val apiKey = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"

        retrofitService.requestResults(apiKey, keyword)
            .enqueue(object : Callback<MapSearchResponse> {
                override fun onResponse(
                    call: Call<MapSearchResponse>,
                    response: Response<MapSearchResponse>
                ) {
                    if (response.isSuccessful) {
                        callback(response)
                    }
                }

                override fun onFailure(call: Call<MapSearchResponse>, t: Throwable) {
                    Log.d("API", "error : $t")
                }
            })
    }
}