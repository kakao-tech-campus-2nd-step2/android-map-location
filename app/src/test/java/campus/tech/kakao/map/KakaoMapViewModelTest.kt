package campus.tech.kakao.map

import campus.tech.kakao.map.repository.kakaomap.LastPositionRepository
import campus.tech.kakao.map.viewmodel.kakaomap.KakaoMapViewModel
import com.kakao.vectormap.LatLng
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

private const val TEST_X = 36.37003
private const val TEST_Y = 127.34594

@ExperimentalCoroutinesApi
class KakaoMapViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val lastPositionRepository = mockk<LastPositionRepository>(relaxed = true)
    private val kakaoMapViewModel = KakaoMapViewModel(lastPositionRepository)

    @Test
    fun testSaveLastLocation() {
        // given
        val position = LatLng.from(TEST_X, TEST_Y)

        // when
        kakaoMapViewModel.saveLastPosition(position)

        // then
        verify { lastPositionRepository.saveLastPosition(position) }
    }

    @Test
    fun testLoadLastLocation() {
        // given
        val position = LatLng.from(TEST_X, TEST_Y)
        coEvery { lastPositionRepository.loadLastPosition() } returns position

        // when
        kakaoMapViewModel.loadLastPosition()

        // then
        assert(kakaoMapViewModel.lastPosition.value == position)
        coVerify { lastPositionRepository.loadLastPosition() }
    }
}