package campus.tech.kakao.map.view

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.data.net.KakaoApiLauncher
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.use_case.GetAllPlacesUseCase
import campus.tech.kakao.map.domain.use_case.GetLogsUseCase
import campus.tech.kakao.map.domain.use_case.GetPlacesUseCase
import campus.tech.kakao.map.domain.use_case.UpdateLogsUseCase
import campus.tech.kakao.map.domain.use_case.RemoveLogUseCase
import campus.tech.kakao.map.domain.use_case.UpdatePlacesUseCase
import com.kakao.sdk.common.KakaoSdk.init
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.switchMap

class PlaceViewModel(
    application: Application,
    private val getPlacesUseCase: GetPlacesUseCase,
    private val getAllPlacesUseCase: GetAllPlacesUseCase,
    private val getLogsUseCase: GetLogsUseCase,
    private val updatePlacesUseCase: UpdatePlacesUseCase,
    private val updateLogsUseCase: UpdateLogsUseCase,
    private val removeLogUseCase: RemoveLogUseCase
) : AndroidViewModel(application) {

    private val kakaoApiLauncher = KakaoApiLauncher()

    val searchText = MutableLiveData<String>()

    private val _logList = MutableLiveData<List<Place>>()
    val logList: LiveData<List<Place>> get() = _logList

    private val _places = searchText.asFlow()
        .debounce(500L)
        .flatMapLatest { query ->
            if (query.isNotBlank()) {
                liveData {
                    updateTempPlaces(query)
                    delay(300L)
                    emit(getAllLiveDataPlaces())
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

    private fun getAllLiveDataPlaces(): List<Place>{
        return getAllPlacesUseCase()
    }

    fun updatePlaces(places: List<Place>) {
        updatePlacesUseCase.invoke(places)
    }

    fun getLogs(): List<Place> {
        return getLogsUseCase.invoke()
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
        updateLogsUseCase.invoke(updatedList)
    }

    fun removeLog(id: String) {
        removeLogUseCase.invoke(id)
        _logList.value = getLogs()
    }
    private fun updateTempPlaces(keyword: String, page: Int = 1): List<Place>{
        val tempPlaces = mutableListOf<Place>()
        val MAX_PAGE = 3
        val SIZE = 15
        for (i in page..MAX_PAGE) {
            kakaoApiLauncher.getPlacesBySearchText(
                BuildConfig.KAKAO_REST_API_KEY, keyword,SIZE, i,
                onSuccess = { places ->
                    tempPlaces.addAll(places)
                    if (i == MAX_PAGE) {
                        updatePlaces(tempPlaces)
                        tempPlaces.clear()
                    }
                },
                onError = { errorMessage ->
                    Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }
        return tempPlaces
    }

    companion object {
        fun provideFactory(application: PlaceApplication): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PlaceViewModel(
                            application,
                            application.getPlacesUseCase,
                            application.getAllPlacesUseCase,
                            application.getLogsUseCase,
                            application.updatePlacesUseCase,
                            application.updateLogsUseCase,
                            application.removeLogUseCase
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
