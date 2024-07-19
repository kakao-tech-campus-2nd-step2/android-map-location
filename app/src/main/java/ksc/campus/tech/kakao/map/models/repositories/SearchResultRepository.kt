package ksc.campus.tech.kakao.map.models.repositories

import android.util.Log
import androidx.lifecycle.LiveData

data class SearchResult(val id: String, val name: String, val address: String, val type: String, val latitude: Double, val longitude: Double)

interface SearchResultRepository {
    val searchResult: LiveData<List<SearchResult>>
    fun search(text: String, apiKey: String)

    companion object{
        private var _instance: SearchResultRepository? = null

        fun injectDependency(repository: SearchResultRepository){
            if (_instance == null) {
                _instance = repository
            }
        }

        fun getInstance(): SearchResultRepository{
            if(_instance == null){
                Log.e("KSC","instance is null")
                throw NullPointerException()
            }
            return _instance!!
        }
    }
}