package campus.tech.kakao.map

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkService {
    suspend fun searchKakaoMapItem(category: String): MutableList<KakaoMapItem> {
        val mapItemList = mutableListOf<KakaoMapItem>()

        val response = withContext(Dispatchers.IO) {
            retrofitService.requsetKakaoMap(query = category).execute()
        }
        if(response.isSuccessful) {
            val body = response.body()
            //val maxPage = body?.meta?.pageable_count ?: 1
            val maxPage = 3
            for(i in 1..maxPage) {
                val responseEachPage = withContext(Dispatchers.IO) {
                    retrofitService.requsetKakaoMap(query = category, page = i).execute()
                }
                responseEachPage.body()?.documents?.forEach {
                    mapItemList.add(
                        KakaoMapItem(it.id, it.place_name, it.address_name, it.category_group_name)
                    )
                }

            }
        }
        return mapItemList
    }
}