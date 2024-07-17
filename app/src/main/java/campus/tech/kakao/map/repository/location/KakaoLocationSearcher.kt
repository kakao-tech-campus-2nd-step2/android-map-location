package campus.tech.kakao.map.repository.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.model.kakaolocal.Item
import campus.tech.kakao.map.model.kakaolocal.KakaoLocalResponse
import campus.tech.kakao.map.network.KakaoLocal
import campus.tech.kakao.map.network.KakaoLocalService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoLocationSearcher : Location {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    override fun search(keyword: String) {
        val retrofit = KakaoLocal.instance
        val service = retrofit.create(KakaoLocalService::class.java)
        val call = service.searchKeyword(query = keyword)
        call.enqueue(
            object : Callback<KakaoLocalResponse> {
                override fun onResponse(
                    call: Call<KakaoLocalResponse>,
                    response: Response<KakaoLocalResponse>,
                ) {
                    val items = mutableListOf<Item>()
                    if (response.isSuccessful) {
                        val body = response.body()

                        body?.documents?.forEach {
                            items.add(Item(it.placeName, it.addressName, it.categoryGroupName))
                        }
                    }
                    _items.postValue(items)
                }

                override fun onFailure(
                    call: Call<KakaoLocalResponse>,
                    t: Throwable,
                ) {
                    t.printStackTrace()
                }
            },
        )
    }

    companion object {
        fun getInstance(): KakaoLocationSearcher {
            return KakaoLocationSearcher()
        }
    }
}
