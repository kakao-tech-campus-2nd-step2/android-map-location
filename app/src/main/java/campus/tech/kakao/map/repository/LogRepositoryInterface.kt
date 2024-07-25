package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.db.entity.Place

interface LogRepositoryInterface {
    fun getAllLogs(): List<Place>
    fun haveAnyLog() : Boolean
    fun insertLog(place: Place): List<Place>
    fun deleteLog(place: Place): List<Place>
}