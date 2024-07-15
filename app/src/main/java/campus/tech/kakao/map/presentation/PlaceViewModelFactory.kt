package campus.tech.kakao.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.domain.usecase.*;

class PlaceViewModelFactory(private val getSearchPlacesUseCase: GetSearchPlacesUseCase,
                            private val saveSearchQueryUseCase: SaveSearchQueryUseCase,
                            private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
                            private val removeSearchQueryUseCase: RemoveSearchQueryUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
            return PlaceViewModel(
                getSearchPlacesUseCase,
                saveSearchQueryUseCase,
                getSearchHistoryUseCase,
                removeSearchQueryUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
