package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.MapRepository
import campus.tech.kakao.map.repository.MapRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepositoryInterface
): ViewModel() {

    fun getLastLocation(): Pair<Double, Double>? {
        return mapRepository.getLastLocation()
    }

}