package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.datasource.LastLocationLocalDataSource

class LastLocationRepository(
    private val lastLocationLocalDataSource: LastLocationLocalDataSource
) {
    fun putLastLocation(location: Location){
        lastLocationLocalDataSource.putLastLocation(location)
    }

    fun getLastLocation(): Location? {
        return lastLocationLocalDataSource.getLastLocation()
    }
}