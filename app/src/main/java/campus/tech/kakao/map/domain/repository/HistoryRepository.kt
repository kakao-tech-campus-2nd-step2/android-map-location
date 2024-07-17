package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Location

interface HistoryRepository {
    fun insertHistory(newHistory: Location)
    fun deleteHistory(oldHistory: Location)
    fun getAllHistory(): List<Location>
}