package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.SavedLocation
import campus.tech.kakao.map.model.repository.SavedLocationRepository

class SavedLocationViewModel(
    private val savedLocationRepository: SavedLocationRepository
) : ViewModel() {
    private val _savedLocation = MutableLiveData<MutableList<SavedLocation>>()
    val savedLocation: LiveData<MutableList<SavedLocation>> get() = _savedLocation

    fun setSavedLocation() {
        _savedLocation.value = savedLocationRepository.getSavedLocationAll()
    }
    fun addSavedLocation(title: String) {
        val savedLocation = SavedLocation(title)
        if(_savedLocation.value?.contains(savedLocation) == false){
            savedLocationRepository.addSavedLocation(title)
            val currentList = _savedLocation.value ?: return
            if (currentList.add(savedLocation)) {
                _savedLocation.value = currentList
            }
        }
    }
    fun deleteSavedLocation(savedLocation: SavedLocation) {

        if (savedLocationRepository.deleteSavedLocation(savedLocation.title)) {
            val currentList = _savedLocation.value ?: return
            if (currentList.remove(savedLocation)) {
                _savedLocation.value = currentList
            }
        }
    }
}
