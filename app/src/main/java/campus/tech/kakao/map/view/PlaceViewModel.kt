package campus.tech.kakao.map.view

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.PlaceApplication.Companion.isNetworkActive
import campus.tech.kakao.map.data.net.KakaoApiClient
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.model.ResultSearchKeyword
import campus.tech.kakao.map.domain.repository.PlaceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class PlaceViewModel(private val repository: PlaceRepository) : ViewModel() {

    val searchText = MutableLiveData<String>()

    private val _uiState = MutableStateFlow(UiState(true,false))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _logList = MutableLiveData<List<Place>>()
    val logList: LiveData<List<Place>> get() = _logList

    private val _places = searchText.asFlow()
        .debounce(500L)
        .flatMapLatest { query ->
            if (query.isNotBlank()) {
                flow {
                    val places = getPlaces(query)
                    emit(places)
                }
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope,SharingStarted.Lazily, emptyList())
    val places: StateFlow<List<Place>> get() = _places


    init {
        _logList.value = getLogs()
    }

    fun clearSearch() {
        searchText.value = ""
    }
    suspend fun getPlaces(keyword: String): List<Place>{
        return withContext(Dispatchers.IO) { repository.getPlaces(keyword) }
    }
    fun getLogs(): List<Place> {
        return repository.getLogs()
    }

    fun updateLogs(place: Place) {
        val updatedList = _logList.value?.toMutableList() ?: mutableListOf()
        val existingLog = updatedList.find { it.id == place.id }
        if (existingLog != null) {
            updatedList.remove(existingLog)
            updatedList.add(0, existingLog)
        } else {
            updatedList.add(0, place)
        }
        _logList.value = updatedList
        repository.updateLogs(updatedList)
    }

    fun removeLog(id: String) {
        repository.removeLog(id)
        _logList.value = getLogs()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val placeRepository = (this[APPLICATION_KEY] as PlaceApplication).placeRepository
                PlaceViewModel(repository = placeRepository)
            }
        }
    }
}
