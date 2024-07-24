package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.datasource.LocationDataSource

class LocationRepository(
    private val locationRemoteDataSource: LocationDataSource
) {
    suspend fun getLocationRemote(query: String): List<Location> {
        return locationRemoteDataSource.getLocations(query)
    }
}