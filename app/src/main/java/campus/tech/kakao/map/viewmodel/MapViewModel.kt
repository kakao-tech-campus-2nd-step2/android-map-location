package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import campus.tech.kakao.map.repository.MapRepository

class MapViewModel(
    private val application: Application,
    private val mapRepository: MapRepository
): AndroidViewModel(application) {

    fun getLastLocation(): Pair<Double, Double>? {
        return mapRepository.getLastLocation()
    }

}