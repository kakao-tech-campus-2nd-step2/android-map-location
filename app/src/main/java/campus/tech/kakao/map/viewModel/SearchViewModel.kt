package campus.tech.kakao.map.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import campus.tech.kakao.map.dataRepository.SearchDataRepository

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SearchDataRepository = SearchDataRepository()

    //"DataSearchActivity"에서 사용할 LiveData
    val searchResults = repository.searchResults

    //검색 결과를 LiveData에 저장
    fun loadResultData(searchQuery:String){
        repository.loadResultMapData(searchQuery)
    }
}