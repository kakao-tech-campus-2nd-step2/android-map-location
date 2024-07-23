package campus.tech.kakao.map.viewModel

import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.PreferenceRepository


class MapViewModel(repository: PreferenceRepository) : ViewModel() {
    private val prefersRepo = repository

    fun saveLocation(locationKey: String, data: String) {
        prefersRepo.setString(locationKey, data)
    }

    fun getLocation(locationKey: String, data: String?): String {
        return prefersRepo.getString(locationKey, data)
    }
}