package campus.tech.kakao.map.data

import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.domain.RetrofitInstance
import campus.tech.kakao.map.domain.RetrofitLocalKeywordService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRepository {

    private val retrofitLocalKeywordService: RetrofitLocalKeywordService = RetrofitInstance.retrofitLocalKeywordService

    fun searchPlace(
        keyword: String,
        onSuccess: (List<PlaceDataModel>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val call = retrofitLocalKeywordService.searchPlaceByKeyword("KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}", keyword)
        call.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.isSuccessful) {
                    val keywordList: MutableList<PlaceDataModel> = mutableListOf()
                    val places = response.body()?.documents
                    places?.let {
                        for (placeInfo in places) {
                            val place = PlaceDataModel(
                                name = placeInfo.placeName,
                                category = placeInfo.categoryGroupName,
                                address = placeInfo.addressName,
                                x = placeInfo.x,
                                y = placeInfo.y
                            )
                            keywordList.add(place)
                        }
                    }
                    onSuccess(keywordList)
                    Log.d("API response", "Success: $places")
                }
                else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("API response", "Error response: $errorBody")
                }
            }

            override fun onFailure(call: Call<SearchResult>, throwable: Throwable) {
                Log.w("API response", "Failure: $throwable")
                onFailure(throwable)
            }
        })
    }
}
