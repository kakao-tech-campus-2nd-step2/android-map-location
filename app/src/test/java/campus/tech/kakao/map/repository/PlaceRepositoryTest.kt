package campus.tech.kakao.map.repository

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import campus.tech.kakao.map.db.PlaceDBHelper
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class PlaceRepositoryTest {

    private lateinit var dbHelper: PlaceDBHelper
    private lateinit var placeRepository : PlaceRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        dbHelper = mockk(relaxed = true)
        placeRepository = PlaceRepository(dbHelper)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `카카오 데이터소스에서 데이터를 호출하는지`() = runTest{
        val text = "전남대"
        println(placeRepository.getKakaoLocalPlaceData(text).toString())
        coVerify { placeRepository.kakaoApiDataSource.getPlaceData(text) }
    }
}