package campus.tech.kakao.map.data.net

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.model.ResultSearchKeyword
import campus.tech.kakao.map.util.PlaceMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoApiLauncher {
    private val client = RetrofitApiClient.api

    fun getPlacesBySearchText(
        apiKey: String,
        keyword: String,
        size: Int = 1,
        page: Int = 1,
        onSuccess: (List<Place>) -> Unit,
        onError: (String) -> Unit) {

        val call = client.getSearchKeyword(apiKey, keyword, size , page)

        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val places = PlaceMapper.mapPlaces(it.documents)
                        onSuccess(places)
                    }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                onError(t.message ?: "Error: Unknown")
            }
        })
    }
}
