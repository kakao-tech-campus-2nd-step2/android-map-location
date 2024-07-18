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

    private var _tabViewVisible = MutableLiveData<Boolean>()
    val tabViewVisible: LiveData<Boolean> = _tabViewVisible

    init {
        initClickLog()
    }

    // 앱 키면 가장 먼저 할 것 : ⓐ db 비우기 ⓑ 검색결과 채우기 ⓒ 클릭결과 가져오기
    private fun initClickLog() {
        initResearchList() // researchList를 새로 불러오고
        updateTabRecyclerView()   // tabview를 보일지 아닐지
    }

    private fun updateTabRecyclerView() {
        val isVisible = mainModel.hasAnyClick()
        _tabViewVisible.postValue(isVisible)
    }
    private fun initResearchList() {
        mainModel.getResearchLogs()
        _logList.postValue(mainModel.getLogList())
    }

    fun callResultList(userInput: String){
        mainModel.callKakao(userInput) {
            _placeList.value = it
        }
    }

    // recyclerVIewAdapter 인자에 들어갈 내용 -> ⓐ Click_DB에 넣고 ⓑ 로컬 ResearchList에 넣기
    fun resultItemClickListener(item: Place) {
        mainModel.insertLog(item)

        val updateTabItem = mainModel.getLogList()
        _logList.value = updateTabItem

        updateTabRecyclerView()
    }

    fun inputCloseButtonClickListener() {
        _placeList.postValue(emptyList())
    }

    fun deleteLogClickListner(item: Place){
        mainModel.deleteResearchEntry(item)

        val removeLog = mainModel.getLogList()
        _logList.value = removeLog

        updateTabRecyclerView()
    }
}