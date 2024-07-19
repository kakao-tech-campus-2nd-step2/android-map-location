package ksc.campus.tech.kakao.map.models.repositoriesImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ksc.campus.tech.kakao.map.models.repositories.SearchResult
import ksc.campus.tech.kakao.map.models.repositories.SearchResultRepository

class FakeSearchResultRepository: SearchResultRepository {
    private val _searchResult: MutableLiveData<List<SearchResult>> = MutableLiveData(listOf())

    override val searchResult: LiveData<List<SearchResult>>
        get() = _searchResult

    private fun getDummyData(prefix:String):List<SearchResult>{
        val result = mutableListOf<SearchResult>()
        for(i in 0..15) {
            result.add(
                SearchResult(
                    i.toString(),
                    "name $prefix $i",
                    "address $prefix $i",
                    "type $i",
                    0.0,
                    0.0
                )
            )
        }

        return result
    }

    override fun search(text: String, apiKey: String) {
        _searchResult.postValue(getDummyData(text))
    }
}