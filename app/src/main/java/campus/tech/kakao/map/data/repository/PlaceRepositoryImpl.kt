package campus.tech.kakao.map.data.repository

import android.content.Context
import android.util.Log
import campus.tech.kakao.map.data.PreferenceHelper
import campus.tech.kakao.map.data.model.SearchResponse
import campus.tech.kakao.map.data.network.HttpService
import campus.tech.kakao.map.data.network.RetrofitObject
import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRepositoryImpl(private val context: Context) : PlaceRepository {
//    private val retrofitService = RetrofitObject.retrofitService
    override fun searchPlaces(query: String, callback: (List<PlaceVO>?) -> Unit) {
        HttpService.searchKeyword(query = query){ response ->
//            override fun onResponse(
//                call: Call<SearchResponse>,
//                response: Response<SearchResponse>
//            ) {
                if (response != null) {
                    val places = response?.documents?.map {
                        PlaceVO(
                            placeName = it.placeName,
                            addressName = it.addressName,
                            categoryName = it.categoryGroupName,
                            latitude = it.y.toDouble(),
                            longitude = it.x.toDouble()
                        )
                    }
                    callback(places)
                } else {
                    Log.d("testt", "Response failed: ${response.toString()}")
                    callback(null)
                }
            }
        }


    override fun saveSearchQuery(place: PlaceVO) {
        PreferenceHelper.saveSearchQuery(context, place.placeName)
    }

    override fun getSearchHistory(): List<String> {
        return PreferenceHelper.getSearchHistory(context)
    }

    override fun removeSearchQuery(query: String) {
        PreferenceHelper.removeSearchQuery(context, query)
    }

    override fun saveLastPlace(place: PlaceVO) {
        PreferenceHelper.saveLastPlace(context, place)
    }

    override fun getLastPlace(): PlaceVO? {
        return PreferenceHelper.getLastPlace(context)
    }
}