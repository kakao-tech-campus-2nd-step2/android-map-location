package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Location

interface LastLocationRepository {
    fun insertLastLocation(location: Location)
    fun clearLastLocation()
    fun getLastLocation(): Location?
}