package campus.tech.kakao.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.viewModel.MapViewModel
import java.lang.IllegalArgumentException

class MapViewModelFactory(repository: PreferenceRepository): ViewModelProvider.Factory {
    private val prefersRepo = repository
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MapViewModel::class.java)){
            return MapViewModel(prefersRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}