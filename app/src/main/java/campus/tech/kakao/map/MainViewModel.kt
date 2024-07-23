package campus.tech.kakao.map

import androidx.lifecycle.ViewModel
import com.kakao.vectormap.LatLng

class MainViewModel(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    fun setLocation(latitude: Double? = null, longitude: Double? = null): LatLng? {
        return if (latitude != null && longitude != null) {
            LatLng.from(latitude, longitude)
        } else {
            val historyList = preferenceManager.getArrayList(Constants.SEARCH_HISTORY_KEY)
            if (historyList.isEmpty()) {
                null
            } else {
                val historyLongitude = historyList[0].document.x.toDouble()
                val historyLatitude = historyList[0].document.y.toDouble()
                LatLng.from(historyLatitude, historyLongitude)
            }
        }
    }
}
