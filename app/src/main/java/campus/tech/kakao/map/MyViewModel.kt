package campus.tech.kakao.map

import android.util.Log
import android.widget.Toast
import androidx.core.view.ViewCompat.performHapticFeedback
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel : ViewModel() {

    val searchText = MutableLiveData<String>()
    var isIntent: MutableLiveData<Boolean> = MutableLiveData(false)
    var placeAdapterUpdateData = MutableLiveData<List<Place>>()
    var savedSearchAdapterUpdateData = MutableLiveData<List<SavedSearch>>()

    var itemClick =MutableLiveData<Place>() //Place의 item

    var nameClick =MutableLiveData<SavedSearch>() //savedSearch의 이름 부분
    var closeClick =MutableLiveData<SavedSearch>() // savedSearch의 x부분

    fun intentSearchPlace() {
        isIntent.value = true
    }

    fun clickCloseIcon(){

        //햅틱 진동 기능 추가하고 싶다..
        searchText.value =" " //editText빈칸으로 만들기
    }

    fun searchPlaces(query: String) {
        val call = RetrofitInstance.api.searchKeyword(query) //API 요청

        call.enqueue(object : Callback<KakaoSearchResponse> { //비동기 API 요청

            override fun onResponse(
                call: Call<KakaoSearchResponse>,
                response: Response<KakaoSearchResponse>
            ) {

                Log.d("seyoung", "query : $query")
                if (response.isSuccessful) {    //성공했을 때
                    val places = response.body()?.documents?.map { document ->
                        Place(
                            id = document.id.toInt(),
                            name = document.place_name,
                            address = document.address_name,
                            kind = document.category_name,
                            longitude = document.x, //경도
                            latitude = document.y   //위도
                        )
                    } ?: emptyList()
                    placeAdapterUpdateData.value = places //검색결과
                } else {  //실패했을 때
                    Log.d("seyoung", "Error: ${response.errorBody()?.string()}")
                }

            }

            override fun onFailure(
                call: Call<KakaoSearchResponse>,
                t: Throwable
            ) { //실패 했을 때 (네트워크 문제, 비행기 모드...)
                Log.d("seyoung", "Failure: ${t.message}")
                //토스트메시지 띄우기
            }
        })
    }


    //저장된 검색어 업데이트
    fun updateSavedSearch(dbManager: DatabaseManager) {
        val savedSearches = dbManager.getSavedSearches()
        savedSearchAdapterUpdateData.value = savedSearches
    }


}