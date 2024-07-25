package campus.tech.kakao.map.kakaoAPI

import campus.tech.kakao.map.database.KakaoMapItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkService {
    var page: Int = 1
    var isEnd: Boolean? = false

    suspend fun searchKakaoMapItem(category: String): MutableList<KakaoMapItem> {
        val mapItemList = mutableListOf<KakaoMapItem>()
        page = 1
        isEnd = false

        do {
            val documents = searchKakaoMapItemByPage(category)
            documents?.forEach {
                mapItemList.add(
                    KakaoMapItem(it.id, it.place_name, it.address_name, it.category_group_name, it.x, it.y)
                )
            }
        } while (documents != null)

        return mapItemList
    }

    private suspend fun searchKakaoMapItemByPage(category: String): List<Document>? {
        val responseEachPage = withContext(Dispatchers.IO) {
            retrofitService.requsetKakaoMap(query = category, page = page).execute()
        }
        if (responseEachPage.isSuccessful && isEnd == false) {
            //Log.d("uin", "" + page)
            isEnd = responseEachPage.body()?.meta?.is_end
            if (isEnd == false) {
                page++
            }
            return responseEachPage.body()?.documents
        }
        return null
    }
}