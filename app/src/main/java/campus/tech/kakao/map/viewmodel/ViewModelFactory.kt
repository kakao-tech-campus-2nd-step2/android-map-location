package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import campus.tech.kakao.map.model.datasource.LocationLocalDataSource
import campus.tech.kakao.map.model.repository.LocationRepository
import campus.tech.kakao.map.model.repository.SavedLocationRepository

class ViewModelFactory {
    class LocationViewModelFactory(
        private val locationRepository: LocationRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                return LocationViewModel(locationRepository) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }

    class SavedLocationViewModelFactory(
        private val savedLocationRepository: SavedLocationRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(SavedLocationViewModel::class.java)) {
                return SavedLocationViewModel(savedLocationRepository) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}
