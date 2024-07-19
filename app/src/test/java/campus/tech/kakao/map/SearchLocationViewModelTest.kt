package campus.tech.kakao.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import campus.tech.kakao.map.model.SearchLocationRepository
import campus.tech.kakao.map.viewmodel.SearchLocationViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

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
}