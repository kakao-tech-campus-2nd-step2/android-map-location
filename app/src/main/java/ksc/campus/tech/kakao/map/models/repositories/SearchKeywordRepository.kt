package ksc.campus.tech.kakao.map.models.repositories

import android.util.Log
import androidx.lifecycle.LiveData

interface SearchKeywordRepository {
    val keywords: LiveData<List<String>>
    fun addKeyword(keyword: String)
    fun deleteKeyword(keyword: String)
    fun getKeywords()
}