package ksc.campus.tech.kakao.map.models.repositories

import android.util.Log
import androidx.lifecycle.LiveData

interface SearchKeywordRepository {
    val keywords: LiveData<List<String>>
    suspend fun addKeyword(keyword: String)
    suspend fun deleteKeyword(keyword: String)
    suspend fun getKeywords()
}