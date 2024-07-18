package campus.tech.kakao.map.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import campus.tech.kakao.map.MapApplication
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.domain.repository.HistoryRepository
import campus.tech.kakao.map.domain.repository.LastLocationRepository
import campus.tech.kakao.map.domain.repository.ResultRepository
import kotlinx.coroutines.launch

class MapViewModel(
    dbHelper: MapDbHelper,
    private val resultRepository: ResultRepository,
    private val historyRepository: HistoryRepository,
    private val lastLocationRepository: LastLocationRepository
) : ViewModel() {
    private val _searchResult = MutableLiveData<List<Location>>()
    val searchResult: LiveData<List<Location>> = _searchResult
    private val _searchHistory = MutableLiveData<List<Location>>()
    val searchHistory: LiveData<List<Location>> = _searchHistory
    private val _lastLocation = MutableLiveData<Location>()
    val lastLocation = _lastLocation as LiveData<Location>

    fun searchKeyword(keyword: String) {
        viewModelScope.launch {
            resultRepository.search(keyword)
            _searchResult.value = resultRepository.getAllResult()
        }
    }

    fun getAllResult(): List<Location> {
        return resultRepository.getAllResult()
    }

    fun insertHistory(newHistory: Location) {
        historyRepository.insertHistory(newHistory)
        _searchHistory.value = historyRepository.getAllHistory()
    }

    fun deleteHistory(oldHistory: Location) {
        historyRepository.deleteHistory(oldHistory)
        _searchHistory.value = historyRepository.getAllHistory()
    }

    fun getAllHistory(): List<Location> {
        return historyRepository.getAllHistory()
    }

    fun insertLastLocation(location: Location) {
        lastLocationRepository.insertLastLocation(location)
        _lastLocation.value = lastLocationRepository.getLastLocation()
        Log.d("ViewModel", _lastLocation.value.toString())
    }

    fun getLastLocation(): Location? {
        _lastLocation.value = lastLocationRepository.getLastLocation()
        return lastLocationRepository.getLastLocation()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MapApplication)
                MapViewModel(
                    application.dbHelper,
                    application.resultRepositoryImpl,
                    application.historyRepositoryImpl,
                    application.lastLocationRepositoryImpl
                )
            }
        }
    }
}