package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.SavedLocation
import campus.tech.kakao.map.model.datasource.LocationLocalDataSource

class SavedLocationRepository(
    private val locationLocalRepository: LocationLocalDataSource
) {
    fun getSavedLocationAll(): MutableList<SavedLocation> {
        val results = locationLocalRepository.getSavedLocationAll()
        return if(results.isNotEmpty()) results else mutableListOf()
    }

    fun addSavedLocation(title: String) {
        locationLocalRepository.addSavedLocation(title)
    }

    fun deleteSavedLocation(title: String): Boolean {
        if (locationLocalRepository.deleteSavedLocation(title) == 1) {
            return true
        }
        return false
    }

}