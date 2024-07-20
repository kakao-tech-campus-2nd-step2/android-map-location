package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.repository.PlaceRepository

class MainViewModel(
    private val application: Application,
    private val placeRepository: PlaceRepository
): AndroidViewModel(application) {

    private val _placeList = MutableLiveData<List<Place>>()
    val placeList: LiveData<List<Place>> = _placeList

    private var _logList = MutableLiveData<List<Place>>()
    val logList: LiveData<List<Place>> = _logList

    private var _tabViewVisible = MutableLiveData<Boolean>()
    val tabViewVisible: LiveData<Boolean> = _tabViewVisible

    private var _placeListVisible = MutableLiveData<Boolean>()
    val placeListVisible: LiveData<Boolean> = _placeListVisible

    init {
        initSearchLog()
    }

    // 앱 키면 가장 먼저 할 것 : ⓐ db 비우기 ⓑ 검색결과 채우기 ⓒ 클릭결과 가져오기
    private fun initSearchLog() {
        initSearchList() // researchList를 새로 불러오고
        updateTabViewVisible()   // tabview를 보일지 아닐지
    }

    private fun updateTabViewVisible() {
        val isVisible = placeRepository.hasStoredLogs()
        _tabViewVisible.postValue(isVisible)
    }

    private fun initSearchList() {
        _logList.postValue(placeRepository.getResearchLogs())
    }

    fun callResultList(userInput: String){
        if (userInput == ""){
            _placeList.value = emptyList()
        } else{
            placeRepository.searchPlaces(userInput) {
                _placeList.value = it
            }
        }
    }

    fun showPlaceList(){
        _placeListVisible.value = !_placeList.value.isNullOrEmpty()
    }

    fun callLogList() = placeRepository.getLogList()

    fun resultItemClickListener(item: Place) {
        placeRepository.insertLog(item)

        val updateTabItem = placeRepository.getLogList()
        _logList.value = updateTabItem

        updateTabViewVisible()
    }

    fun closeButtonClickListener() {
        _placeList.postValue(emptyList())
    }

    fun deleteLog(item: Place){
        placeRepository.deleteResearchEntry(item)

        val removeLog = placeRepository.getLogList()
        _logList.value = removeLog

        updateTabViewVisible()
    }
}