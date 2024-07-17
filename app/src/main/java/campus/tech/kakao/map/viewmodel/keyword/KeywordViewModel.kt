package campus.tech.kakao.map.viewmodel.keyword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.kakaolocal.Item
import campus.tech.kakao.map.repository.keyword.KeywordRepository
import campus.tech.kakao.map.viewmodel.OnKeywordItemClickListener
import campus.tech.kakao.map.viewmodel.OnSearchItemClickListener

class KeywordViewModel(private val keywordRepository: KeywordRepository) :
    ViewModel(),
    OnSearchItemClickListener,
    OnKeywordItemClickListener {
    private val _keyword = MutableLiveData<List<String>>()
    val keyword: LiveData<List<String>>
        get() = _keyword

    private val _keywordClicked = MutableLiveData<String>()
    val keywordClicked: LiveData<String>
        get() = _keywordClicked

    private fun updateKeywordHistory(keyword: String) {
        keywordRepository.delete(keyword)
        keywordRepository.update(keyword)
        _keyword.value = keywordRepository.read()
    }

    private fun deleteKeywordHistory(keyword: String) {
        keywordRepository.delete(keyword)
        _keyword.value = keywordRepository.read()
    }

    fun readKeywordHistory() {
        _keyword.value = keywordRepository.read()
    }

    override fun onSearchItemClick(item: Item) {
        updateKeywordHistory(item.place)
    }

    override fun onKeywordItemDeleteClick(keyword: String) {
        deleteKeywordHistory(keyword)
    }

    override fun onKeywordItemClick(keyword: String) {
        _keywordClicked.value = keyword
        updateKeywordHistory(keyword)
    }
}
