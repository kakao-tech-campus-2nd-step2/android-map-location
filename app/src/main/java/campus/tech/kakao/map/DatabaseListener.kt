package campus.tech.kakao.map

import campus.tech.kakao.map.domain.model.Location

interface DatabaseListener {
    fun deleteHistory(oldHistory: Location)
    fun insertHistory(newHistory: Location)
    fun searchHistory(locName: String, isExactMatch: Boolean)
    fun showMap(newHistory: Location)
    fun insertLastLocation(location: Location)
}