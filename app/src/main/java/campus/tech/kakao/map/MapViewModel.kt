package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MapViewModel(
    private val application: Application,
    private val mapModel: MapModel
): AndroidViewModel(application) {
}