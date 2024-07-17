package campus.tech.kakao.map.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.kakaolocal.Item
import campus.tech.kakao.map.repository.location.KakaoLocationSearcher

class SearchViewModel(private val locationSearcher: KakaoLocationSearcher) : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    init {
        locationSearcher.items.observeForever {
            _items.value = it
        }
    }

    fun searchLocationData(keyword: String) {
        if (keyword.isNotEmpty()) {
            locationSearcher.search(keyword)
        } else {
            _items.value = emptyList()
        }
    }
}
