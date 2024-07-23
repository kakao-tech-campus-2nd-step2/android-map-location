package campus.tech.kakao.map

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.Model.RetrofitClient
import campus.tech.kakao.map.Model.SearchResult
import campus.tech.kakao.map.viewmodel.DataDbHelper
import campus.tech.kakao.map.viewmodel.MainViewModel
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Call

class ViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    private val application: Application = mockk(relaxed = true)
    private val dataDbHelper: DataDbHelper = mockk(relaxed = true)
    private val apiService: RetrofitClient = mockk(relaxed = true)
    private val call: Call<SearchResult> = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = MainViewModel(application, dataDbHelper)
    }

    @Test
    fun testUpdateSearchResults() {
        val places = listOf(
            Place("Place1", "Address1", "Category1", "37.5", "127.0"),
            Place("Place2", "Address2", "Category2", "37.6", "127.1")
        )

        var capturedUiState: MainViewModel.UiState? = null
        viewModel.setUiStateChangedListener { uiState ->
            capturedUiState = uiState
        }

        viewModel.updateSearchResults(places)

        assert(capturedUiState?.locationList?.size == 2)
        assert(capturedUiState?.isShowText == false)
        assert(capturedUiState?.locationList?.get(0)?.name == "Place1")
        assert(capturedUiState?.locationList?.get(1)?.name == "Place2")
    }

    @Test
    fun testEmptySearchResults() {
        var capturedUiState: MainViewModel.UiState? = null
        viewModel.setUiStateChangedListener { uiState ->
            capturedUiState = uiState
        }

        viewModel.updateSearchResults(emptyList())

        assert(capturedUiState?.locationList?.isEmpty() == true)
        assert(capturedUiState?.isShowText == true)
    }

    @Test
    fun testDeleteLocation() {
        val location = LocationData("Test", "Address", "Category", 37.5, 127.0)

        every { dataDbHelper.deleteLocation(any()) } just Runs

        viewModel.deleteLocation(location)

        verify(exactly = 1) { dataDbHelper.deleteLocation(location) }
    }

    @Test
    fun testDeleteAllLocations() {
        every { dataDbHelper.deleteAllLocations() } just Runs

        viewModel.deleteAllLocations()

        verify(exactly = 1) { dataDbHelper.deleteAllLocations() }
    }
}