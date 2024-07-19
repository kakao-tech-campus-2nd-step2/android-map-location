package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Location

interface ResultRepository {
    suspend fun search(keyword: String): List<Location>
    fun getAllResult(): List<Location>
}