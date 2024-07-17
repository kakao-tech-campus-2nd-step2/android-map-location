package campus.tech.kakao.map

interface DatabaseListener {
    fun deleteHistory(oldHistory: Location)
    fun insertHistory(newHistory: Location)
    fun searchHistory(locName: String, isExactMatch: Boolean)
    fun showMap(newHistory: Location)
    fun insertLastLocation(location: Location)
}