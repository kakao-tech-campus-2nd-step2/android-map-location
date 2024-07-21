package campus.tech.kakao.map.view

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.PlaceApplication.Companion.isNetworkActive
import campus.tech.kakao.map.data.net.KakaoApiClient
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

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
                liveData {
                    fetchPlaces(query)
                    delay(300L)
                    emit(getAllPlaces())
                }.asFlow()
            } else {
                flowOf(emptyList<Place>())
            }
        }
        .asLiveData()
    val places: LiveData<List<Place>> get() = _places


    init {
        _logList.value = getLogs()
    }

    fun clearSearch() {
        searchText.value = ""
    }

    private fun getAllPlaces(): List<Place>{
        return repository.getAllPlaces()
    }

    fun updatePlaces(places: List<Place>) {
        repository.updatePlaces(places)
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

    fun fetchPlaces(keyword: String){
        Log.d("07/22", "fetchplaces start")
        viewModelScope.launch {
            Log.d("07/22", isNetworkActive().toString())
            if(isNetworkActive()){
                val resultPlaces = mutableListOf<Place>()

                for (page in 1..3){
                    val response = KakaoApiClient.api.getSearchKeyword(
                        key = BuildConfig.KAKAO_REST_API_KEY,
                        query = keyword,
                        size = 15,
                        page = page)
                    Log.d("07/22", "fetchplaces start")

                    if (response.isSuccessful) {
                        response.body()?.documents?.let { resultPlaces.addAll(it) }
                    }
                }

                updatePlaces(resultPlaces)
                Log.d("07/22", "fetchplaces done")
            }
        }
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
