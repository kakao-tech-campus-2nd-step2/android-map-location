package ksc.campus.tech.kakao.map.models.repositories

import android.util.Log
import androidx.lifecycle.LiveData

interface SearchKeywordRepository {
    val keywords: LiveData<List<String>>
    fun addKeyword(keyword: String)
    fun deleteKeyword(keyword: String)
    fun getKeywords()

    companion object{
        private var _instance: SearchKeywordRepository? = null

        fun injectDependency(repository: SearchKeywordRepository){
            if (_instance == null) {
                _instance = repository
            }
        }

        fun getInstance(): SearchKeywordRepository{
            if(_instance == null){
                Log.e("KSC","instance is null")
                throw NullPointerException()
            }
            return _instance!!
        }
    }
}