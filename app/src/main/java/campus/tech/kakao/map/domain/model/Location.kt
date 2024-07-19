package campus.tech.kakao.map.domain.model

import java.io.Serializable

data class Location(
    val id: String,
    val name: String,
    val category: String,
    val address: String,
    val x: Double,
    val y: Double
) : Serializable {
    companion object {
        const val LOCATION: String = "LOCATION"
        const val NORMAL: String = "일반"
    }
}