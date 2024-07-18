package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.ui.map.MapActivity.MarkerData

interface LocationRepository {
    fun saveLocation(markerData: MarkerData)

    fun loadLocation(): MarkerData
}
