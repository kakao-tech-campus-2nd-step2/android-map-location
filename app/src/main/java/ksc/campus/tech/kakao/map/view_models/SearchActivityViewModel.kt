package ksc.campus.tech.kakao.map.view_models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraPosition
import ksc.campus.tech.kakao.map.BuildConfig
import ksc.campus.tech.kakao.map.models.LocationInfo
import ksc.campus.tech.kakao.map.models.SearchKeywordRepository
import ksc.campus.tech.kakao.map.models.SearchResult
import ksc.campus.tech.kakao.map.models.SearchResultRepository
import ksc.campus.tech.kakao.map.models.MapViewRepository


class SearchActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val mapViewRepository: MapViewRepository =
        MapViewRepository.getInstance()
    private val searchResultRepository: SearchResultRepository =
        SearchResultRepository.getInstance()
    private val keywordRepository: SearchKeywordRepository =
        SearchKeywordRepository.getInstance(application)

    private val _searchText: MutableLiveData<String> = MutableLiveData("")
    private val _activeContent: MutableLiveData<ContentType> = MutableLiveData(ContentType.MAP)

    val searchResult: LiveData<List<SearchResult>>
        get() = searchResultRepository.searchResult
    val keywords: LiveData<List<String>>
        get() = keywordRepository.keywords
    val searchText: LiveData<String>
        get() = _searchText
    val activeContent: LiveData<ContentType>
        get() = _activeContent

    val selectedLocation: LiveData<LocationInfo?>
        get() = mapViewRepository.selectedLocation
    val cameraPosition: LiveData<CameraPosition>
        get() = mapViewRepository.cameraPosition


    init {
        keywordRepository.getKeywords()
        mapViewRepository.loadFromSharedPreference(application)
    }

    private fun search(query: String) {
        searchResultRepository.search(query, BuildConfig.KAKAO_REST_API_KEY)
        switchContent(ContentType.SEARCH_LIST)
    }

    private fun addKeyword(keyword: String) {
        keywordRepository.addKeyword(keyword)
    }

    private fun deleteKeyword(keyword: String) {
        keywordRepository.deleteKeyword(keyword)
    }

    private fun updateLocation(address:String, name:String, latitude:Double, longitude:Double){
        mapViewRepository.updateSelectedLocation(getApplication(),LocationInfo(address, name, latitude, longitude))
        mapViewRepository.updateCameraPositionWithFixedZoom(latitude, longitude)
    }

    fun clickSearchResultItem(selectedItem: SearchResult) {
        addKeyword(selectedItem.name)
        Log.d("KSC", "lat: ${selectedItem.latitude}, lon: ${selectedItem.longitude}")
        updateLocation(selectedItem.address, selectedItem.name, selectedItem.latitude, selectedItem.longitude)
        switchContent(ContentType.MAP)
    }

    fun submitQuery(value: String) {
        search(value)
    }

    fun clickKeywordDeleteButton(keyword: String) {
        deleteKeyword(keyword)
    }

    fun clickKeyword(keyword: String) {
        _searchText.postValue(keyword)
        search(keyword)
    }

    fun switchContent(type: ContentType) {
        _activeContent.postValue(type)
    }

    fun updateCameraPosition(position: CameraPosition){
        mapViewRepository.updateCameraPosition(getApplication(), position)
    }

    enum class ContentType { MAP, SEARCH_LIST }
}