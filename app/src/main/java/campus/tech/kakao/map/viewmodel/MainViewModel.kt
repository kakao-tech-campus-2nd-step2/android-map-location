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

    private fun initSearchLog() {
        initSearchList()
        updateTabViewVisible()
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

    fun resultItemClickListener(item: Place) {
        placeRepository.insertLog(item)

        val updateTabItem = placeRepository.getLogList()
        _logList.value = updateTabItem

        updateTabViewVisible()
    }

    fun clearPlaceList() {
        _placeList.postValue(emptyList())
    }

    fun deleteLog(item: Place){
        placeRepository.deleteResearchEntry(item)

        val removeLog = placeRepository.getLogList()
        _logList.value = removeLog

        updateTabViewVisible()
    }

    fun saveLastLocation(item: Place) {
        placeRepository.saveLastLocation(item)
    }
}