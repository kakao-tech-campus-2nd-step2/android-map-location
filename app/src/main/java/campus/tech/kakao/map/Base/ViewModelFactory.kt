package campus.tech.kakao.map.Base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.Data.PlaceRepositoryImpl
import campus.tech.kakao.map.Domain.PlaceRepository
import campus.tech.kakao.map.Presenter.ViewModel.MapViewModel
import campus.tech.kakao.map.ViewModel.SearchViewModel

class ViewModelFactory(private val repository: PlaceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MapViewModel::class.java)){
            return MapViewModel(repository) as T
        }
        return null as T
    }
}