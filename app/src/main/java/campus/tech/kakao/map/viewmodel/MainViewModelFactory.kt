package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.PlaceRepositoryInterface

class MainViewModelFactory(private val application: Application, private val model: PlaceRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
            return PlaceViewModel(model) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}