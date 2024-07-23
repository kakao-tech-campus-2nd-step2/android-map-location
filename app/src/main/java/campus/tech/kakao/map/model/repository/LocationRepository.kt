package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.datasource.LocationLocalDataSource
import campus.tech.kakao.map.model.datasource.LocationRemoteDataSource

class LocationRepository(
    private val locationRemoteRepository: LocationRemoteDataSource
) {
    suspend fun getLocationRemote(query: String): List<Location> {
        return locationRemoteRepository.getLocations(query)
    }
}