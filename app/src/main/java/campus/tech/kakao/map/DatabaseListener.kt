package campus.tech.kakao.map

interface DatabaseListener {
    fun deleteHistory(oldHistory: Location)
    fun insertHistory(newHistory: Location)
}