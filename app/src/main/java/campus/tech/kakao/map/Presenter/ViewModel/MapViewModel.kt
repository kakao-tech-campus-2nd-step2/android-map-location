package campus.tech.kakao.map.Presenter.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.Domain.PlaceRepository

class MapViewModel(private val repository: PlaceRepository) : ViewModel() {
    private val _currentPlace : MutableLiveData<Place?> = MutableLiveData()
    val currentPlace : LiveData<Place?> = _currentPlace

    fun initPlace(id : Int){
        _currentPlace.value = repository.getFavoriteById(id)
    }

}