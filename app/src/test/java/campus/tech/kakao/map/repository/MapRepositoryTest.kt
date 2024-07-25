package campus.tech.kakao.map.repository

import android.app.Application
import campus.tech.kakao.map.network.KakaoApiService
import campus.tech.kakao.map.model.MapItem
import campus.tech.kakao.map.model.KakaoSearchResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class MapRepositoryTest {

    @Mock
    private lateinit var apiService: KakaoApiService

    private lateinit var repository: MapRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = MapRepositoryImpl(mock(Application::class.java))
    }

    @Test
    fun testSearchItems() = runBlockingTest {
        val items = listOf(
            MapItem("1", "바다 정원", "강원도 고성군", "카페", 127.0, 37.0)
        )

        `when`(apiService.searchPlaces(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Response.success(KakaoSearchResponse(items)))

        val result = repository.searchItems("바다 정원")

        assertEquals(items, result)
    }
}
