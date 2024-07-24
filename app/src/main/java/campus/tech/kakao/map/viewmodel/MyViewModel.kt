package campus.tech.kakao.map.viewmodel

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.data.Place
import campus.tech.kakao.map.model.data.SavedSearch
import campus.tech.kakao.map.model.database.DatabaseManager
import campus.tech.kakao.map.model.network.KakaoSearchResponse
import campus.tech.kakao.map.model.network.RetrofitInstance
import campus.tech.kakao.map.model.repository.MyRepository
import campus.tech.kakao.map.view.PlaceAdapter
import campus.tech.kakao.map.view.SavedSearchAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel(private val context: Context, private val repository: MyRepository) : ViewModel() {

    val searchText = MutableLiveData<String>()
    var isIntent: MutableLiveData<Boolean> = MutableLiveData(false)
    var placeAdapterUpdateData = MutableLiveData<List<Place>>()
    var savedSearchAdapterUpdateData = MutableLiveData<List<SavedSearch>>()
    var itemClick = MutableLiveData<Place>() //Place의 item
    var nameClick = MutableLiveData<SavedSearch>() //savedSearch의 이름 부분
    var closeClick = MutableLiveData<SavedSearch>() // savedSearch의 x부분
//    private val _searchResults = MutableLiveData<List<Place>>()


    val vmPlaceAdapter: PlaceAdapter = PlaceAdapter(listOf()) { place ->  //리사이클러뷰의 아이템을 누르면
        repository.insertSavedsearch(place.id, place.name)
        updateSavedSearch()
        setSharedPreferences(place)
        itemClick.value = place
    }

    val vmSavedSearchAdapter: SavedSearchAdapter = SavedSearchAdapter(listOf(),
        onCloseClick = { SavedSearch -> //SavedSearch의 x를 누르면
            repository.deleteSavedSearch(SavedSearch.id)
            updateSavedSearch()
        },
        onNameClick = { SavedSearch ->   //SavedSearch의 이름을 누르면
            nameClick.value = SavedSearch
            searchText.value = SavedSearch.name
        }
    )

    fun setSharedPreferences(place: Place) {
        //sharedPreference를 이용해서 name,address,latitude,longitude 저장하기
        val sharedPreferences =context.getSharedPreferences(
            "PlacePreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString("name", place.name)
        editor.putString("address", place.address)
        editor.putString("longitude", place.longitude)
        editor.putString("latitude", place.latitude)
        editor.apply()
    }


    fun intentSearchPlace() {   // true일 때 SearchPlaceActivity
        isIntent.value = true
    }

    fun clickCloseIcon() {
        //햅틱 진동 기능 추가하고 싶다..
        searchText.value = " " //editText빈칸으로 만들기
    }

    fun searchPlace(query: String) {
        viewModelScope.launch {
            try {
                val response: Response<KakaoSearchResponse> = repository.searchKeyword(query)
                if (response.isSuccessful) {
                    placeAdapterUpdateData.value = response.body()?.documents?.map { document ->
                        Place(
                            id = document.id.toInt(),
                            name = document.place_name,
                            address = document.address_name,
                            kind = document.category_name,
                            longitude = document.x, //경도
                            latitude = document.y   //위도
                        )
                    } ?: emptyList()
                } else {
                    Log.e("MyViewModel", "Search failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Exception in searchPlace", e)
            }
        }
    }



    //저장된 검색어 업데이트
    fun updateSavedSearch() {
        savedSearchAdapterUpdateData.value = repository.getSavedSearches()
    }


}