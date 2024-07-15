package campus.tech.kakao.map

interface DatabaseListener {
    fun deleteHistory(historyName: String)
    fun insertHistory(historyName: String)
}