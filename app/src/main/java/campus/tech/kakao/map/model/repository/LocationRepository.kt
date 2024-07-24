package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.datasource.LocationLocalDataSource
import campus.tech.kakao.map.model.datasource.LocationRemoteDataSource

class LocationRepository(
    private val locationRemoteDataSource: LocationRemoteDataSource
) {
    suspend fun getLocationRemote(query: String): List<Location> {
        return locationRemoteDataSource.getLocations(query)
    }
}