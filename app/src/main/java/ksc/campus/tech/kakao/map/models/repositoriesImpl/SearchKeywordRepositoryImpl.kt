package ksc.campus.tech.kakao.map.models.repositoriesImpl

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.models.SearchDbHelper
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository

class SearchKeywordRepositoryImpl(context: Context): SearchKeywordRepository {
    private val _keywords: MutableLiveData<List<String>> = MutableLiveData(listOf())
    override val keywords: LiveData<List<String>>
        get() = _keywords

    private var searchDb: SearchDbHelper

    init {
        searchDb = SearchDbHelper(context)
    }

    private fun queryKeyWordAndPostValue() {
        val newData = searchDb.queryAllSearchKeywords()
        _keywords.postValue(newData)
    }

    override suspend fun addKeyword(keyword: String) {
        searchDb.insertOrReplaceKeyword(keyword)
        queryKeyWordAndPostValue()
    }

    override suspend fun deleteKeyword(keyword: String) {
        searchDb.deleteKeyword(keyword)
        queryKeyWordAndPostValue()
    }

    override suspend fun getKeywords() {
        val newData = searchDb.queryAllSearchKeywords()
        _keywords.postValue(newData)
    }
}