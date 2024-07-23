package campus.tech.kakao.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.TestRule
import android.app.Application
import android.content.Context
import io.mockk.junit4.MockKRule
import org.junit.Assert.*

private const val TEST_SEARCH = "SEARCH"
private const val TEST_PLACE_NAME = "PLACE_NAME"
private const val TEST_PLACE_ADDRESS = "PLACE_ADDRESS"
private const val TEST_PLACE_CATEGORY = "PLACE_CATEGORY"

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private val placeRepository = mockk<PlaceRepository>(relaxed = true)
    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val application = mockk<Application>(relaxed = true)
        val context = mockk<Context>(relaxed = true)

        every { application.applicationContext } returns context

        viewModel = MainViewModel(application)
        viewModel.repository = placeRepository
        viewModel.preferencesRepository = preferencesRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun when_SearchPlace_Expect_ReturnResult() = runTest {
        // Given
        val query = TEST_SEARCH
        val expectedResults = listOf(
            Document(
                place_name = TEST_PLACE_NAME,
                distance = "",
                place_url = "",
                category_name = "",
                address_name = TEST_PLACE_ADDRESS,
                road_address_name = "",
                id = "",
                phone = "",
                category_group_code = "",
                category_group_name = TEST_PLACE_CATEGORY,
                x = "",
                y = ""
            )
        )
        coEvery { placeRepository.searchPlaces(query) } returns expectedResults

        val observer = mockk<Observer<List<Document>>>(relaxed = true)
        viewModel.searchResults.observeForever(observer)

        // When
        viewModel.searchPlaces(query)

        // Then
        coVerify { placeRepository.searchPlaces(query) }
        val actualResults = viewModel.searchResults.value
        assertNotNull(actualResults)
        assertEquals(expectedResults, actualResults)
    }

    @Test
    fun when_LoadSavedSearches_Expect_ReturnSavedSearch() = runTest {
        // Given
        val savedSearches = listOf("검색1", "검색2")
        every { preferencesRepository.getSavedSearches() } returns savedSearches

        val observer = mockk<Observer<List<String>>>(relaxed = true)
        viewModel.savedSearches.observeForever(observer)

        // When
        viewModel.loadSavedSearches()
        advanceUntilIdle()

        // Then
        val actualSearches = viewModel.savedSearches.value
        assertNotNull(actualSearches)
        assertEquals(savedSearches, actualSearches)
    }
}
