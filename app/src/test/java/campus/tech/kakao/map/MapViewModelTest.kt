package campus.tech.kakao.map

import android.util.Log
import campus.tech.kakao.map.model.DataStoreRepository
import campus.tech.kakao.map.viewmodel.MapViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.Test
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class MapViewModelTest {
    private val mockRepository = mockk<DataStoreRepository>()
    private val viewModel = MapViewModel(mockRepository)

    @Test
    fun testSaveLastLocation() {
        // given
        Dispatchers.setMain(Dispatchers.Unconfined)
        coEvery { mockRepository.saveLocation(any(), any()) } just Runs
        val latitude = 123.0
        val longitude = 456.0

        // when
        viewModel.saveLastLocation(latitude, longitude)

        // then
        coVerify { mockRepository.saveLocation(latitude, longitude) }
        Dispatchers.resetMain()
    }

    @Test
    fun testSaveLastLocation_IOException() {
        // given
        Dispatchers.setMain(Dispatchers.Unconfined)
        coEvery { mockRepository.saveLocation(any(), any()) } throws IOException("Test Exception")
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        val latitude = 123.0
        val longitude = 456.0

        // when
        viewModel.saveLastLocation(latitude, longitude)

        // then
        coVerify { mockRepository.saveLocation(latitude, longitude) }
        verify { Log.e("MapViewModel", "Failed to save location", any()) }
        Dispatchers.resetMain()
    }
}