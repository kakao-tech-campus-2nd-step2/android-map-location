package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.SharedPreferenceRepository

class MapViewModelFactory (
    private val sharedPreferenceRepository: SharedPreferenceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapActivityViewModel::class.java)) {
            return MapActivityViewModel(sharedPreferenceRepository) as T
        }
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}