package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.repository.LogRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logRepository: LogRepositoryInterface
): ViewModel() {
    private var _logList = MutableLiveData<List<Place>>()
    val logList: LiveData<List<Place>> get() = _logList

    private var _tabViewVisible = MutableLiveData<Boolean>()
    val tabViewVisible: LiveData<Boolean> get() = _tabViewVisible

    init {
        initLogData()
    }

    private fun initLogData(){
        initLogList()
        updateLogViewVisible()
    }

    private fun initLogList(){
        _logList.postValue(logRepository.getAllLogs())
    }

    private fun updateLogViewVisible(){
        val isVisible = logRepository.haveAnyLog()
        _tabViewVisible.postValue(isVisible)
    }

    fun deleteLog(item: Place){
        val logListAfterRemoving = logRepository.deleteLog(item)
        _logList.value = logListAfterRemoving

        updateLogViewVisible()
    }

    fun insertLog(item: Place) {
        val logListAfterInserting = logRepository.insertLog(item)
        _logList.value = logListAfterInserting

        updateLogViewVisible()
    }
}