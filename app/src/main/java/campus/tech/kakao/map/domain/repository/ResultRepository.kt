package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Location

interface ResultRepository {
    fun getAllResult(): List<Location>
}