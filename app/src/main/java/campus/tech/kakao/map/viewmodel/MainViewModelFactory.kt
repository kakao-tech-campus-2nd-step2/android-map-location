package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.PlaceRepository

class MainViewModelFactory(private val application: Application, private val model: PlaceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, model) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}