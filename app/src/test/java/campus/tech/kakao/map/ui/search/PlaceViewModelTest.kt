package campus.tech.kakao.map.ui.search

import android.util.Log
import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.model.Place
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var placeRepository: PlaceRepository
    private lateinit var viewModel: PlaceViewModel

    @Before
    fun setup() {
        placeRepository = mockk()
        viewModel = PlaceViewModel(placeRepository)

        Dispatchers.setMain(testDispatcher)
        mockLogClass()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSearchPlacesByCategory() =
        runTest {
            // given
            val categoryInput = "대형마트"
            val totalPageCount = 1
            val placeList =
                listOf(
                    Place(
                        id = "1234",
                        name = "마트 1",
                        category = "대형마트",
                        address = "주소1",
                        longitude = "12.3456",
                        latitude = "123.4567",
                    ),
                    Place(
                        id = "1235",
                        name = "마트 2",
                        category = "대형마트",
                        address = "주소2",
                        longitude = "12.3457",
                        latitude = "123.4568",
                    ),
                )

            coEvery {
                placeRepository.getPlacesByCategory(
                    categoryInput,
                    totalPageCount,
                )
            } returns placeList

            // when
            viewModel.searchPlacesByCategory(categoryInput, totalPageCount)

            // then
            coVerify { placeRepository.getPlacesByCategory(categoryInput, totalPageCount) }
            assertEquals(placeList, viewModel.searchResults.value)
        }

    @Test
    fun `searchPlacesByCategory는 예외를 처리한다`() =
        runTest {
            // given
            val categoryInput = "restaurant"
            val totalPageCount = 1
            val exception = Exception("Network error")

            coEvery {
                placeRepository.getPlacesByCategory(
                    categoryInput,
                    totalPageCount,
                )
            } throws exception

            // when
            viewModel.searchPlacesByCategory(categoryInput, totalPageCount)

            // then
            viewModel.searchResults.take(1).collect {
                assert(it.isEmpty())
            }

            coVerify { placeRepository.getPlacesByCategory(categoryInput, totalPageCount) }
        }

    private fun mockLogClass() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }
}
