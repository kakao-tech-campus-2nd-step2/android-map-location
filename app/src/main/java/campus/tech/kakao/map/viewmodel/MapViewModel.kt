package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.KakaoMapProductResponse
import campus.tech.kakao.map.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _searchResults = MutableLiveData<List<MapItem>>()
    val searchResults: LiveData<List<MapItem>> get() = _searchResults

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun searchPlaces(keyword: String) {
        val apiKey = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        RetrofitClient.apiService.searchPlaces(apiKey, keyword).enqueue(object :
            Callback<KakaoMapProductResponse> {
            override fun onResponse(call: Call<KakaoMapProductResponse>, response: Response<KakaoMapProductResponse>) {
                if (response.isSuccessful) {
                    val documents = response.body()?.documents ?: emptyList()
                    val results = documents.map { MapItem(it.place_name, it.address_name, it.category_group_name, it.x.toDouble(), it.y.toDouble()) }
                    _searchResults.postValue(results)
                } else {
                    _searchResults.postValue(emptyList())
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<KakaoMapProductResponse>, t: Throwable) {
                _searchResults.postValue(emptyList())
                _errorMessage.postValue("네트워크 요청 실패: ${t.message}")
            }
        })
    }
}

data class MapItem(
    val name: String,
    val address: String,
    val category: String,
    val longitude: Double,
    val latitude: Double
)
