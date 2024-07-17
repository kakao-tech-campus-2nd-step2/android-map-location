package campus.tech.kakao.map.viewmodel

import retrofit2.Call
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.Model.RetrofitClient
import campus.tech.kakao.map.Model.SearchCallback
import campus.tech.kakao.map.Model.SearchResult

class MainViewModel: ViewModel() {

    private var listener: (UiState) -> Unit = {}
    private lateinit var call: Call<SearchResult>

    private var uiState: UiState = UiState(
        locationList = emptyList(),
        isShowText = false
    )

    fun setUiStateChangedListener(listener: (UiState) -> Unit) {
        this.listener = listener
    }

    fun searchLocations(key: String) {
        val apiService = RetrofitClient.instance
        call = apiService.searchPlaces(BuildConfig.API_KEY, key)
        call.enqueue(SearchCallback(this))
    }

    fun updateSearchResults(results: List<Place>) {
        val locationList = mutableListOf<LocationData>()
        for (result in results) {
            locationList.add(
                LocationData(
                    result.place_name,
                    result.address_name,
                    result.category_group_name
                )
            )
        }
        uiState = UiState(
            locationList = locationList,
            isShowText = locationList.isEmpty()
        )

        listener(uiState)
    }

    override fun onCleared() {
        super.onCleared()
        if (::call.isInitialized) {
            call.cancel()
        }
    }

    data class UiState(
        val locationList: List<LocationData>,
        val isShowText: Boolean
    )
}