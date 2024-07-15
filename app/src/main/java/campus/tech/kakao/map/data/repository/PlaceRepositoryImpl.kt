package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.data.network.PlaceResponse
import campus.tech.kakao.map.data.network.service.KakaoLocalService
import campus.tech.kakao.map.model.Place
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class PlaceRepositoryImpl
    @Inject
    constructor(
        private val kakaoLocalService: KakaoLocalService,
    ) : PlaceRepository {
        private val categoryCodeMap =
            mapOf(
                "대형마트" to "MT1", "편의점" to "CS2", "어린이집" to "PS3",
                "유치원" to "PS3", "학교" to "SC4", "학원" to "AC5",
                "주차장" to "PK6", "주유소" to "OL7", "충전소" to "OL7",
                "지하철역" to "SW8", "은행" to "BK9", "문화시설" to "CT1",
                "중개업소" to "AG2", "공공기관" to "PO3", "관광명소" to "AT4",
                "숙박" to "AD5", "음식점" to "FD6", "카페" to "CE7",
                "병원" to "HP8", "약국" to "PM9",
            )

        override suspend fun getPlacesByCategory(
            categoryInput: String,
            totalPageCount: Int,
        ): List<Place> {
            val categoryGroupCode = categoryCodeMap[categoryInput] ?: return emptyList()
            return fetchPlacesByCategory(categoryGroupCode, totalPageCount)
        }

        private suspend fun fetchPlacesByCategory(
            categoryGroupCode: String,
            totalPageCount: Int,
        ): List<Place> {
            val deferredResults =
                createDeferredResults(categoryGroupCode, totalPageCount)

            return processDeferredResults(deferredResults)
        }

        private suspend fun createDeferredResults(
            categoryGroupCode: String,
            totalPageCount: Int,
        ): List<Deferred<PlaceResponse?>> =
            coroutineScope {
                (1..totalPageCount).map { page ->
                    async {
                        try {
                            kakaoLocalService.getPlacesByCategory(categoryGroupCode, page)
                        } catch (e: IOException) {
                            Log.e("NetworkException", "IOException occurred: ${e.message}")
                            null
                        } catch (e: Exception) {
                            Log.e("CoroutineException", "Exception occurred: ${e.message}")
                            null
                        }
                    }
                }
            }

        private suspend fun processDeferredResults(deferredResults: List<Deferred<PlaceResponse?>>): List<Place> {
            val placeList = mutableListOf<Place>()

            val responses = deferredResults.awaitAll()

            responses.forEach { response ->
                if (response != null) {
                    handleSuccessfulResponse(response, placeList)
                } else {
                    Log.e("CoroutineException", "Exception occurred in coroutine")
                }
            }

            return placeList
        }

        private fun handleSuccessfulResponse(
            response: PlaceResponse,
            allPlaces: MutableList<Place>,
        ) {
            response.documents.mapTo(allPlaces) { dto ->
                Place(
                    id = dto.placeId,
                    name = dto.placeName,
                    address = dto.address,
                    category = dto.category,
                )
            }
        }
    }
