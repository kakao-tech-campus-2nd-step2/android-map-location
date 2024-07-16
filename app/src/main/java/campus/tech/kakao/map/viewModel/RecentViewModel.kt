package campus.tech.kakao.map.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.dataRepository.RecentDataRepository
import campus.tech.kakao.map.data.RecentSearchData

class RecentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecentDataRepository = RecentDataRepository(application)

    private val _recentDataList = MutableLiveData<List<RecentSearchData>>()

    init {
        _recentDataList.value = repository.getRecentSearchDataList()
    }

    fun addRecentData(data: String, address: String, time: Long) {
        val currentList = _recentDataList.value.orEmpty().toMutableList()
        val selectData = RecentSearchData(data, address, time)

        if (checkExist(currentList, selectData)) {
            repository.insertSearchData(selectData)
        } else {
            repository.updateTime(selectData)
        }
        _recentDataList.value = repository.getRecentSearchDataList()
    }

    //DB에 데이터 추가 전 중복 검사 (현재 DB에 없으면 true)
    private fun checkExist(
        currentList: MutableList<RecentSearchData>,
        data: RecentSearchData
    ): Boolean {
        return !currentList.any { it.name == data.name && it.address == data.address }
    }

    fun getRecentDataLiveData(): LiveData<List<RecentSearchData>> {
        return _recentDataList
    }

    fun deleteRecentData(data: String, address: String) {
        repository.deleteSearchData(data, address)
        _recentDataList.value = repository.getRecentSearchDataList()
    }
}