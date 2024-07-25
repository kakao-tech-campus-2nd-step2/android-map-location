package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import campus.tech.kakao.map.model.MapItem
import campus.tech.kakao.map.repository.MapRepository
import campus.tech.kakao.map.viemodel.MapViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class MapViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: MapRepository

    private lateinit var viewModel: MapViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val application = mock(Application::class.java)
        viewModel = MapViewModel(application, repository)
    }

    @Test
    fun testSearchItems() = runBlockingTest {
        val items = listOf(
            MapItem("1", "바다 정원", "강원도 고성군", "카페", 127.0, 37.0)
        )

        `when`(repository.searchItems("바다 정원")).thenReturn(items)

        val observer = mock(Observer::class.java) as Observer<List<MapItem>>
        viewModel.searchResults.observeForever(observer)

        viewModel.searchQuery.value = "바다 정원"

        verify(repository).searchItems("바다 정원")
        verify(observer).onChanged(items)
    }

    @Test
    fun testSelectItem() {
        val item = MapItem("1", "바다 정원", "강원도 고성군", "카페", 127.0, 37.0)

        viewModel.selectItem(item)

        val observer = mock(Observer::class.java) as Observer<List<MapItem>>
        viewModel.selectedItems.observeForever(observer)

        verify(observer).onChanged(listOf(item))
    }

    @Test
    fun testRemoveSelectedItem() {
        val item = MapItem("1", "바다 정원", "강원도 고성군", "카페", 127.0, 37.0)

        viewModel.setSelectedItems(listOf(item))

        viewModel.removeSelectedItem(item)

        val observer = mock(Observer::class.java) as Observer<List<MapItem>>
        viewModel.selectedItems.observeForever(observer)

        verify(observer).onChanged(emptyList())
    }

    @Test
    fun testSetSelectedItems() {
        val items = listOf(
            MapItem("1", "바다 정원", "강원도 고성군", "카페", 127.0, 37.0)
        )

        viewModel.setSelectedItems(items)

        val observer = mock(Observer::class.java) as Observer<List<MapItem>>
        viewModel.selectedItems.observeForever(observer)

        verify(observer).onChanged(items)
    }
}
