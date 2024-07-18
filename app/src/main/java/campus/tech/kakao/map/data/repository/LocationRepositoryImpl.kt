package campus.tech.kakao.map.data.repository

import android.content.SharedPreferences
import campus.tech.kakao.map.ui.map.MapActivity.MarkerData
import javax.inject.Inject

class LocationRepositoryImpl
    @Inject
    constructor(
        private val sharedPreferences: SharedPreferences,
    ) : LocationRepository {
        override fun saveLocation(markerData: MarkerData) {
            with(sharedPreferences.edit()) {
                putString(MARKER_PLACE_NAME, markerData.name)
                putString(MARKER_LATITUDE, markerData.latitude.toString())
                putString(MARKER_LONGITUDE, markerData.longitude.toString())
                putString(MARKER_ADDRESS, markerData.address)
                apply()
            }
        }

        override fun loadLocation(): MarkerData {
            val placeName = sharedPreferences.getString(MARKER_PLACE_NAME, null)
            val latitude = sharedPreferences.getString(MARKER_LATITUDE, null)
            val longitude = sharedPreferences.getString(MARKER_LONGITUDE, null)
            val address = sharedPreferences.getString(MARKER_ADDRESS, null)

            return if (placeName != null && latitude != null && longitude != null && address != null) {
                MarkerData(placeName, latitude.toDouble(), longitude.toDouble(), address)
            } else {
                MarkerData("부산대 컴공관", 35.230934, 129.082476, "부산광역시 금정구 부산대학로 63번길 2")
            }
        }

        companion object {
            const val PREF_NAME = "location_prefs"
            private const val MARKER_PLACE_NAME = "placeName"
            private const val MARKER_LATITUDE = "latitude"
            private const val MARKER_LONGITUDE = "longitude"
            private const val MARKER_ADDRESS = "address"
        }
    }
