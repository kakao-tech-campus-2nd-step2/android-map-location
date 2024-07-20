package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.MapRepository
import campus.tech.kakao.map.base.MyApplication

class MapViewModelFactory(private val application: Application, private val mapModel: MapRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(application as MyApplication, mapModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}