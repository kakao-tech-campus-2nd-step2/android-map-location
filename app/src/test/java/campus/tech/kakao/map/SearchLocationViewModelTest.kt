package campus.tech.kakao.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.SearchLocationRepository
import campus.tech.kakao.map.viewmodel.SearchLocationViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class SearchLocationViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<SearchLocationRepository>()
    private lateinit var viewModel: SearchLocationViewModel

    @Before
    fun setUp() {
        every { mockRepository.getHistory() } returns emptyList()
        viewModel = SearchLocationViewModel(mockRepository)
    }

    @Test
    fun testInitViewModel() {
        // given
        val tempMockRepository = mockk<SearchLocationRepository>()
        every { tempMockRepository.getHistory() } returns listOf("History1", "History2")

        // when
        val tempViewModel = SearchLocationViewModel(tempMockRepository)

        // then
        verify { tempMockRepository.getHistory() }
        tempViewModel.history.observeForever(mockk<Observer<List<String>>>(relaxed = true))
        assertEquals(listOf("History1", "History2"), tempViewModel.history.value)
    }

    @Test
    fun testSearchLocation() {
        // given
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel.location.observeForever(mockk<Observer<List<Location>>>(relaxed = true))
        coEvery { mockRepository.searchLocation("testCategory") } returns listOf(
            Location("Location1", "Address1", "Category1", 123.0, 456.0),
            Location("Location2", "Address2", "Category2", 135.0, 246.0)
        )

        // when
        viewModel.searchLocation("testCategory")

        // then
        coVerify { mockRepository.searchLocation("testCategory") }
        assertEquals(2, viewModel.location.value?.size)
        assertEquals("Location1", viewModel.location.value?.get(0)?.name)
        assertEquals("Location2", viewModel.location.value?.get(1)?.name)
        Dispatchers.resetMain()
    }

    @Test
    fun testAddHistory() {
        // given
        every { mockRepository.addHistory(any()) } just Runs
        every { mockRepository.getHistory() } returns listOf("History1", "History2")
        viewModel.history.observeForever(mockk<Observer<List<String>>>(relaxed = true))

        // when
        viewModel.addHistory("History2")

        // then
        verify { mockRepository.addHistory("History2") }
        verify { mockRepository.getHistory() }
        assertEquals(listOf("History1", "History2"), viewModel.history.value)
    }

    @Test
    fun testRemoveHistory() {
        // given
        every { mockRepository.removeHistory(any()) } just Runs
        every { mockRepository.getHistory() } returns listOf("History1")
        viewModel.history.observeForever(mockk<Observer<List<String>>>(relaxed = true))

        // when
        viewModel.removeHistory("History2")

        // then
        verify { mockRepository.removeHistory("History2") }
        verify { mockRepository.getHistory() }
        assertEquals(listOf("History1"), viewModel.history.value)
    }

    @Test
    fun testAddMarker() {
        // given
        viewModel.markerLocation.observeForever(mockk<Observer<Location>>(relaxed = true))
        val testLocation = Location("name", "address", "category", 1.0, 2.0)

        // when
        viewModel.addMarker(testLocation)

        // then
        assertEquals(testLocation, viewModel.markerLocation.value)
    }

    @Test
    fun testSearchLocationByHistory() {
        // given
        viewModel.searchInput.observeForever(mockk<Observer<String>>(relaxed = true))

        // when
        viewModel.searchLocationByHistory("testHistory")

        // then
        assertEquals("testHistory", viewModel.searchInput.value)
    }
}