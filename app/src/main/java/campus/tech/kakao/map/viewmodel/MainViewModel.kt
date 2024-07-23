package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import retrofit2.Call
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.Model.RetrofitClient
import campus.tech.kakao.map.Model.SearchCallback
import campus.tech.kakao.map.Model.SearchResult

class MainViewModel(application: Application, private val dataDbHelper: DataDbHelper) : AndroidViewModel(application) {

    private var listener: (UiState) -> Unit = {}
    private lateinit var call: Call<SearchResult>
    private val db: DataDbHelper = DataDbHelper(application)

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
                    result.category_group_name,
                    result.y.toDouble(),
                    result.x.toDouble()
                )
            )
        }
        uiState = UiState(
            locationList = locationList,
            isShowText = locationList.isEmpty()
        )

        listener(uiState)
    }

    // DB 관련 메서드들
    fun insertLocation(location: LocationData) {
        db.insertLocation(location)
    }

    fun getAllLocations(): List<LocationData> {
        return db.getAllLocations()
    }

    fun deleteLocation(location: LocationData) {
        dataDbHelper.deleteLocation(location)
    }

    fun deleteAllLocations() {
        dataDbHelper.deleteAllLocations()
    }

    override fun onCleared() {
        super.onCleared()
        if (::call.isInitialized) {
            call.cancel()
        }
        db.close()
    }

    data class UiState(
        val locationList: List<LocationData>,
        val isShowText: Boolean
    )
}