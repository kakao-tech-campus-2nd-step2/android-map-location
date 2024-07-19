package campus.tech.kakao.map

import com.kakao.vectormap.LatLng

class MapRepository {

    fun setPosition(longitude: Double, latitude: Double): LatLng {
        return LatLng.from(longitude, latitude)
    }

}