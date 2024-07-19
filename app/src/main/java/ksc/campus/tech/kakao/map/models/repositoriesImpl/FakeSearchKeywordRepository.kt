package ksc.campus.tech.kakao.map.models.repositoriesImpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository


class FakeSearchKeywordRepository: SearchKeywordRepository {
    private val _keywords: MutableLiveData<List<String>> = MutableLiveData(listOf())
    override val keywords: LiveData<List<String>>
        get() = _keywords

    override suspend fun addKeyword(keyword: String) {
        if(_keywords.value == null)
            return
        Log.d("KSC", "adding $keyword")
        _keywords.postValue(addElementWithoutDuplicates(_keywords.value!!, keyword))
    }

    private fun <T>removeElement(list:List<T>, elem:T): List<T>{
        val index = list.indexOf(elem)
        if(index == -1) {
            return list
        }
        return list.subList(0, index).dropLast(1) + list.subList(index, list.size - 1)
    }

    private fun <T>addElementWithoutDuplicates(list:List<T>, elem:T):List<T>{
        var result = list
        if(list.contains(elem)){
            result = removeElement(list, elem)
        }
        return (result + listOf(elem))
    }

    override suspend fun deleteKeyword(keyword: String) {
        if(_keywords.value == null)
            return

        _keywords.postValue(removeElement(_keywords.value!!, keyword))
    }

    private val dummyData:List<String> = listOf(
        "1", "2", "hello", "world"
    )

    override suspend fun getKeywords() {
        _keywords.postValue(dummyData)
    }

}