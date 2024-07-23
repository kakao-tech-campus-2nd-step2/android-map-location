package campus.tech.kakao.map.Model

import com.kakao.vectormap.LatLng

data class LocationData(
    val name: String,
    val location: String,
    val category: String,
    val latitude: Double,
    val longitude: Double
) {
    fun getLatLng(): LatLng {
        return LatLng.from(latitude, longitude)
    }
}

