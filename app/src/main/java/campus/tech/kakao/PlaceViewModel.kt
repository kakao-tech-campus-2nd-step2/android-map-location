package campus.tech.kakao

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.Model.Place

class PlaceViewModel(private val repository: PlaceRepository) : ViewModel() {

    private val _places = MutableLiveData<List<Place>?>()
    val places: MutableLiveData<List<Place>?> get() = _places

    private val _error = MutableLiveData<Throwable?>()
    val error: MutableLiveData<Throwable?> get() = _error

    fun searchPlaces(apiKey: String, query: String) {
        repository.searchPlaces(apiKey, query) { result, error ->
            if (result != null) {
                _places.value = result
            } else {
                _error.value = error
            }
        }
    }
}