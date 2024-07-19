package campus.tech.kakao.map.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.model.HistoryDbHelper
import campus.tech.kakao.map.model.LocalSearchService
import campus.tech.kakao.map.model.SearchLocationRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNCHECKED_CAST")
class SearchLocationViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchLocationViewModel::class.java)) {
            val historyDbHelper = HistoryDbHelper(context)
            val localSearchService = Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/v2/local/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LocalSearchService::class.java)

            val repository = SearchLocationRepository(historyDbHelper, localSearchService)
            return SearchLocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}