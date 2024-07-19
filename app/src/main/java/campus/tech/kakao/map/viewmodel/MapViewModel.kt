package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.DataStoreRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MapViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun saveLastLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                dataStoreRepository.saveLocation(latitude, longitude)
            } catch (ioException: IOException) {
                Log.e("MapViewModel", "Failed to save location", ioException)
            } catch (exception: Exception) {
                throw exception
            }
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