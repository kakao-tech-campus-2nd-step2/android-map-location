package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.Document
import campus.tech.kakao.map.data.ServerResult
import campus.tech.kakao.map.data.source.RetrofitService
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.domain.repository.ResultRepository

class ResultRepositoryImpl(
    private val retrofit: RetrofitService
) : ResultRepository {
    private val result = mutableListOf<Location>()

    override suspend fun search(keyword: String): List<Location> {
        clearResult()
        request(keyword)
        Log.d("ResultRepository", result.size.toString())
        Log.d("ResultRepository", keyword.toString())
        return result
    }

    override fun getAllResult(): List<Location> {
        return result
    }

    private suspend fun request(keyword: String, currPage: Int = 1) {
        if (keyword.isNotEmpty()) {
            val authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
            val serverResult = retrofit.requestLocationByKeyword(authorization, keyword, currPage)
            Log.d("ResultRepository", serverResult.toString())
            serverResult.docList.forEach {
                result.add(documentToLocation(it))
            }
            if (!serverResult.meta.isEnd)
                requestNextPage(serverResult, currPage)
        }
    }

    private fun clearResult() {
        result.clear()
    }

    private suspend fun requestNextPage(serverResult: ServerResult, page: Int) {
        val keyword = serverResult.meta.sameName.keyword
        val nextPage = page + 1
        request(keyword, nextPage)
    }

    private fun documentToLocation(document: Document): Location {
        val id = document.id
        val name = document.placeName
        val category =
            if (document.categoryGroupName != "") {
                document.categoryGroupName
            } else {
                Location.NORMAL
            }

        val address =
            if (document.roadAddressName != "") {
                document.roadAddressName
            } else {
                document.addressName
            }

        val x = document.x.toDouble()
        val y = document.y.toDouble()

        return Location(id, name, category, address, x, y)
    }
}