package campus.tech.kakao.map

import androidx.lifecycle.ViewModel

class SearchViewModel private constructor(private val searchRepository: SearchRepo) : ViewModel() {
    companion object {
        @Volatile private var instance: SearchViewModel? = null

        fun getInstance(searchRepository: SearchRepo): SearchViewModel {
            return instance ?: synchronized(this) {
                instance ?: SearchViewModel(searchRepository).also { instance = it }
            }
        }
    }
    fun insertSearchData(data: String) {
        searchRepository.insertSearchData(data)
    }

    fun getAllSearchData(): List<String> {
        return searchRepository.getAllSearchData()
    }

    fun bind(s: String) {

    }
}
