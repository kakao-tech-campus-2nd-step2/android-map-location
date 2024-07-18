package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.LastPositionRepository
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class KakaoMapViewModel(
    private val lastPositionRepository: LastPositionRepository
) : ViewModel() {
    private val _lastPosition = MutableStateFlow<LatLng?>(null)
    val lastPosition: StateFlow<LatLng?> get() = _lastPosition

    init {
        loadLastPosition()
    }

    fun saveLastPosition(position: LatLng) {
        lastPositionRepository.saveLastPosition(position)
    }

    fun loadLastPosition() {
        _lastPosition.value = lastPositionRepository.loadLastPosition()
    }
}