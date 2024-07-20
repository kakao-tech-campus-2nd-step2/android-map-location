package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(
    private val application: Application,
    private val mainModel: MainModel
): AndroidViewModel(application) {

    private val _placeList = MutableLiveData<List<Place>>()
    val placeList: LiveData<List<Place>> = _placeList

    private var _logList = MutableLiveData<List<Place>>()
    val logList: LiveData<List<Place>> = _logList

    private var _showTabView = MutableLiveData<Boolean>()
    val showTabView: LiveData<Boolean> = _showTabView

    private var _showPlaceList = MutableLiveData<Boolean>()
    val showPlaceList: LiveData<Boolean> = _showPlaceList

    init {
        initSearchLog()
    }

    // 앱 키면 가장 먼저 할 것 : ⓐ db 비우기 ⓑ 검색결과 채우기 ⓒ 클릭결과 가져오기
    private fun initSearchLog() {
        initSearchList() // researchList를 새로 불러오고
        updateTabViewVisible()   // tabview를 보일지 아닐지
    }

    private fun updateTabViewVisible() {
        val isVisible = mainModel.hasAnyClick()
        _showTabView.postValue(isVisible)
    }

    private fun initSearchList() {
        mainModel.getResearchLogs()
        _logList.postValue(mainModel.getLogList())
    }

    fun callResultList(userInput: String){
        if (userInput == ""){
            _placeList.value = emptyList()
        } else{
            mainModel.searchPlaces(userInput) {
                _placeList.value = it
            }
        }
    }

    fun callLogList() = mainModel.getLogList()

    fun resultItemClickListener(item: Place) {
        mainModel.insertLog(item)

        val updateTabItem = mainModel.getLogList()
        _logList.value = updateTabItem

        updateTabViewVisible()
    }

    fun closeButtonClickListener() {
        _placeList.postValue(emptyList())
    }

    fun deleteLogClickListner(item: Place){
        mainModel.deleteResearchEntry(item)

        val removeLog = mainModel.getLogList()
        _logList.value = removeLog

        updateTabViewVisible()
    }
}