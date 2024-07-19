package ksc.campus.tech.kakao.map.models.repositories

import android.util.Log
import androidx.lifecycle.LiveData

data class SearchResult(val id: String, val name: String, val address: String, val type: String, val latitude: Double, val longitude: Double)

interface SearchResultRepository {
    val searchResult: LiveData<List<SearchResult>>
    fun search(text: String, apiKey: String)
}