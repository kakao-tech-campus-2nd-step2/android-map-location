package campus.tech.kakao.map.ViewModel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng


class PlaceViewModel : ViewModel() {

    private val _searchResult = MutableLiveData<LatLng>()
    val searchResult: LiveData<LatLng>
        get() = _searchResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var lastKnownLocation: Location? = null

    fun searchAddress(query: String) {
        //임의의 좌표로 대체
        val randomLatLng = LatLng(37.5665, 126.9780)
        _searchResult.value = randomLatLng
    }

    fun setLastKnownLocation(location: Location) {
        lastKnownLocation = location
    }

    fun getLastKnownLocation(): Location? {
        return lastKnownLocation
    }
}
