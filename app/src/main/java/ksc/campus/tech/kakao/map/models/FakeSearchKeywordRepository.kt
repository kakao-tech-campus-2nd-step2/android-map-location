package ksc.campus.tech.kakao.map.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository


class FakeSearchKeywordRepositoryImpl: SearchKeywordRepository {
    private val _keywords: MutableLiveData<List<String>> = MutableLiveData(listOf())
    override val keywords: LiveData<List<String>>
        get() = _keywords

    override fun addKeyword(keyword: String) {
        if(_keywords.value == null)
            return

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

    override fun deleteKeyword(keyword: String) {
        if(_keywords.value == null)
            return

        _keywords.postValue(removeElement(_keywords.value!!, keyword))
    }

    private val dummyData:List<String> = listOf(
        "1", "2", "hello", "world"
    )

    override fun getKeywords() {
        _keywords.postValue(dummyData)
    }

}