package campus.tech.kakao.map.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.KakaoLocalObject
import campus.tech.kakao.map.model.PlaceInfo
import campus.tech.kakao.map.model.SavePlace
import campus.tech.kakao.map.model.SearchPlace
import campus.tech.kakao.map.repository.SearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val searchRepo: SearchRepository = SearchRepository(application)
    private val _places: MutableLiveData<List<PlaceInfo>> = MutableLiveData()
    private val _savePlaces: MutableLiveData<List<SavePlace>> = MutableLiveData()
    private val KAKAO_API_KEY = "KakaoAK ${BuildConfig.KAKAO_API_KEY}"
    private val retrofit = KakaoLocalObject.createService()

    val places: LiveData<List<PlaceInfo>> get() = _places
    val savePlaces: LiveData<List<SavePlace>> get() = _savePlaces

    init {
        _savePlaces.value = searchRepo.showSavePlace()
    }

    fun savePlaces(placeName: String) {
        _savePlaces.value = searchRepo.savePlacesAndUpdate(placeName)
    }

    fun deleteSavedPlace(savedPlaceName: String) {
        _savePlaces.value = searchRepo.deleteSavedPlacesAndUpdate(savedPlaceName)
    }

    fun getPlaceList(categoryGroupName: String) {
        retrofit.getPlaceList(KAKAO_API_KEY, categoryGroupName).enqueue(object :
            Callback<SearchPlace> {
            override fun onResponse(call: Call<SearchPlace>, response: Response<SearchPlace>) {
                val placeList = response.body()?.documents
                _places.value = placeList
            }

            override fun onFailure(call: Call<SearchPlace>, t: Throwable) {
                Log.d("kiju", "Request failed: ${t.message}")
            }
        })
    }


}