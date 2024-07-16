package campus.tech.kakao.map.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.DataStoreRepository
import kotlinx.coroutines.launch

class MapViewModel() : ViewModel() {
    private lateinit var dataStore: DataStore<Preferences>
    private val dataStoreRepository by lazy { DataStoreRepository(dataStore) }
    fun setDataStore(dataStore: DataStore<Preferences>) {
        this.dataStore = dataStore
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