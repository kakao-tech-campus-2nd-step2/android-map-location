package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.datasource.LocationLocalDataSource
import campus.tech.kakao.map.model.datasource.LocationRemoteDataSource

class LocationRepository(
    private val locationLocalRepository: LocationLocalDataSource,
    private val locationRemoteRepository: LocationRemoteDataSource
) {
    fun addLocationLocal() {
        for (i in 1..9) {
            locationLocalRepository.addLocation("카페$i", "부산 부산진구 전포대로$i", "카페")
        }
        for (i in 1..9) {
            locationLocalRepository.addLocation("음식점$i", "부산 부산진구 중앙대로$i", "음식점")
        }
    }

    fun searchLocationLocal(query: String): List<Location> {
        val results = locationLocalRepository.searchLocation(query)
        return if (results.isNotEmpty()) results else emptyList()
    }

    fun getLocationLocal(): MutableList<Location> {
        return locationLocalRepository.getLocations()
    }

    suspend fun getLocationRemote(query: String): List<Location> {
        return locationRemoteRepository.getLocations(query)
    }
}