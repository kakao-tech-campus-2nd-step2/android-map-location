package campus.tech.kakao.map.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.DataStoreRepository
import kotlinx.coroutines.launch

class MapViewModel() : ViewModel() {
    private lateinit var dataStoreRepository: DataStoreRepository
    fun setDataStoreRepository(context: Context) {
        this.dataStoreRepository = DataStoreRepository(context)
    }

    fun saveLastLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            dataStoreRepository.saveLocation(latitude, longitude)
        }
    }

    fun loadLastLocation(callback: (Double, Double, Boolean) -> Unit) {
        viewModelScope.launch {
            dataStoreRepository.loadLocation().collect {
                it?.let {
                    callback(it.latitude, it.longitude, false)
                }
            }
        }
    }
}