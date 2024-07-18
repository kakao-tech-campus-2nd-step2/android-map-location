package campus.tech.kakao.map.dataRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.retrofit.CategoryData
import campus.tech.kakao.map.retrofit.Document
import campus.tech.kakao.map.retrofit.RetrofitAPI.getResultFromAPI

class SearchDataRepository(){
    private val _searchDataList = MutableLiveData<List<Document>>()
    val searchResults: LiveData<List<Document>>
        get() = _searchDataList

    //검색 결과 가공 후 LiveData에 저장
    fun loadResultMapData(data: String) {
        getResultFromAPI(data) { response ->
            if (response.isSuccessful) {
                val body = response.body()
                body?.documents?.let { documents ->
                    val updatedDocuments = documents.map { document ->
                        val descriptionFromCode = CategoryData.descriptions[document.categoryCode]
                        val descriptionFromCategory = getTailCategory(document.category)
                        val updateDocument = document.copy(
                            categoryDescription = descriptionFromCode,
                            categoryTail = descriptionFromCategory
                        )
                        updateDocument
                    }
                    _searchDataList.postValue(updatedDocuments)
                }
            }
        }
    }

    //API 데이터에서 "category_name" 문자열이 길기 때문에, 충분한 의미 전달이 되는 키워드를 임의로 추출
    private fun getTailCategory(category: String): String {
        val parts = category.split(">")
        return if (parts.size > 1) parts[1].trim() else parts.last().trim()
    }
}