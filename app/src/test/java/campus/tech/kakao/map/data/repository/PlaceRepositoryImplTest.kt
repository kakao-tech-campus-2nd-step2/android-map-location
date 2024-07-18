package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.data.network.PlaceResponse
import campus.tech.kakao.map.data.network.dto.PlaceDTO
import campus.tech.kakao.map.data.network.service.KakaoLocalService
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class PlaceRepositoryImplTest {
    private lateinit var repository: PlaceRepositoryImpl
    private lateinit var mockService: KakaoLocalService

    @Before
    fun setUp() {
        mockService = mockk()
        repository = PlaceRepositoryImpl(mockService)

        mockLogClass()
    }

    private fun mockLogClass() {
        mockkStatic(Log::class)
        io.mockk.every { Log.v(any(), any()) } returns 0
        io.mockk.every { Log.d(any(), any()) } returns 0
        io.mockk.every { Log.i(any(), any()) } returns 0
        io.mockk.every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `getPlacesByCategory 함수는 여러 페이지의 데이터를 가져와 반환한다`() =
        runTest {
            // Given
            val categoryGroupCode = "MT1"
            val categoryInput = "대형마트"
            val placeResponse1 = PlaceResponse(createPlaceDTOList(categoryInput, 1))
            val placeResponse2 = PlaceResponse(createPlaceDTOList(categoryInput, 2))
            val placeResponse3 = PlaceResponse(createPlaceDTOList(categoryInput, 3))
            val totalPageCount = 3

            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 1) } returns placeResponse1
            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 2) } returns placeResponse2
            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 3) } returns placeResponse3

            // When
            val places = repository.getPlacesByCategory(categoryInput, totalPageCount)

            // Then
            assertEquals(45, places.size)
            places.forEach { place ->
                assertEquals(categoryInput, place.category)
            }
        }

    @Test
    fun `getPlacesByCategory 함수는 IOException을 처리하고 빈 리스트를 반환한다`() =
        runTest {
            // Given
            val categoryGroupCode = "MT1"
            val categoryInput = "대형마트"
            val totalPageCount = 3

            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 1) } throws IOException("Network error")
            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 2) } throws IOException("Network error")
            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 3) } throws IOException("Network error")

            // When
            val places = repository.getPlacesByCategory(categoryInput, totalPageCount)

            // Then
            assertEquals(0, places.size)
            verify(exactly = 3) { Log.e("NetworkException", "IOException occurred: Network error") }
        }

    @Test
    fun `getPlacesByCategory 함수는 일반 Exception을 처리하고 빈 리스트를 반환한다`() =
        runTest {
            // Given
            val categoryGroupCode = "MT1"
            val categoryInput = "대형마트"
            val totalPageCount = 3

            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 1) } throws Exception("Unknown error")
            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 2) } throws Exception("Unknown error")
            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 3) } throws Exception("Unknown error")

            // When
            val places = repository.getPlacesByCategory(categoryInput, totalPageCount)

            // Then
            assertEquals(0, places.size)
            verify(exactly = 3) { Log.e("CoroutineException", "Exception occurred: Unknown error") }
        }

    @Test
    fun `getPlacesByCategory 함수는 일부 페이지에서 예외가 발생해도 나머지 데이터는 올바르게 반환한다`() =
        runTest {
            // Given
            val categoryGroupCode = "MT1"
            val categoryInput = "대형마트"
            val placeResponse1 = PlaceResponse(createPlaceDTOList(categoryInput, 1))
            val placeResponse3 = PlaceResponse(createPlaceDTOList(categoryInput, 3))

            val totalPageCount = 4

            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 1) } returns placeResponse1
            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 2) } throws IOException("Network error")
            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 3) } returns placeResponse3
            coEvery { mockService.getPlacesByCategory(categoryGroupCode, 4) } throws Exception("Unknown error")

            // When
            val places = repository.getPlacesByCategory(categoryInput, totalPageCount)

            // Then
            assertEquals(30, places.size)
            verify(exactly = 1) { Log.e("NetworkException", "IOException occurred: Network error") }
            verify(exactly = 1) { Log.e("CoroutineException", "Exception occurred: Unknown error") }
        }

    private fun createPlaceDTOList(
        categoryInput: String,
        page: Int,
    ): List<PlaceDTO> {
        val placeDTOList = mutableListOf<PlaceDTO>()
        val startNumber = (page - 1) * 15
        var curNumber: Int
        for (i in 1..15) {
            curNumber = startNumber + i
            placeDTOList.add(
                PlaceDTO(
                    placeId = curNumber.toString(),
                    placeName = "마트$curNumber",
                    address = "주소$curNumber",
                    category = categoryInput,
                    longitude = "37.${curNumber}2345",
                    latitude = "127.${curNumber}5678",
                ),
            )
        }
        return placeDTOList
    }
}
