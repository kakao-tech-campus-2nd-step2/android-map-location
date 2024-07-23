package campus.tech.kakao.map.repository

import android.util.Log
import campus.tech.kakao.map.BuildConfig.KAKAO_REST_API_KEY
import campus.tech.kakao.map.dto.Place
import campus.tech.kakao.map.dto.ResultSearchKeyword
import campus.tech.kakao.map.network.KakaoLocalApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class KakaoRepository(private val retrofit: Retrofit) {

    private val api: KakaoLocalApi = retrofit.create(KakaoLocalApi::class.java)

    fun searchPlaces(query: String, callback: (List<Place>) -> Unit) {
        api.searchKeyword("KakaoAK $KAKAO_REST_API_KEY", query)
            .enqueue(object : Callback<ResultSearchKeyword> {
                override fun onResponse(
                    call: Call<ResultSearchKeyword>,
                    response: Response<ResultSearchKeyword>
                ) {
                    if (response.isSuccessful) {
                        val places = response.body()?.documents ?: emptyList()
                        callback(places)
                        Log.d("KakaoRepository", "API call successful. Received ${places.size} places.")
                    } else {
                        callback(emptyList())
                        Log.e("KakaoRepository", "API call failed with code ${response.code()}.")
                    }
                }

                override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                    callback(emptyList())
                    Log.e("KakaoRepository", "API call failed: ${t.message}", t)
                }
            })
    }

    companion object {
        private const val TAG = "KakaoRepository"
    }
}
