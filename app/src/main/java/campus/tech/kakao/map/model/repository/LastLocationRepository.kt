package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.datasource.LastLocationlDataSource

class LastLocationRepository(
    private val locationLocalDataSource: LastLocationlDataSource
) {
    fun putLastLocation(location: Location){
        locationLocalDataSource.putLastLocation(location)
    }

    fun getLastLocation(): Location? {
        return locationLocalDataSource.getLastLocation()
    }
}