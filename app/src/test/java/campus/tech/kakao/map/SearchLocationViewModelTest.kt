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
import org.junit.Rule

class SearchLocationViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<SearchLocationRepository>()
    private lateinit var viewModel: SearchLocationViewModel

    @Test
    fun testInitViewModel() {
        // given
        every { mockRepository.getHistory() } returns listOf("Place1", "Place2")

        // when
        viewModel = SearchLocationViewModel(mockRepository)

        // then
        verify { mockRepository.getHistory() }
        viewModel.history.observeForever(mockk<Observer<List<String>>>(relaxed = true))
        assertEquals(listOf("Place1", "Place2"), viewModel.history.value)
    }
}