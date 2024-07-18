package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MapViewModelFactory(private val application: Application, private val mapModel: MapModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(application as MyApplication, mapModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}