package campus.tech.kakao.map

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.database.KakaoMapItemDbHelper
import campus.tech.kakao.map.database.SelectItemDao
import campus.tech.kakao.map.kakaoAPI.NetworkService
import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test

class NetworkServiceTest {
    private lateinit var networkService : NetworkService

    @Before
    fun init() {
        networkService = NetworkService()
    }

    @Test
    fun checkInsertSelectItem() {
        CoroutineScope(Dispatchers.IO).launch {
            val kakaoMapItems = networkService.searchKakaoMapItem("카페")
            assertTrue(kakaoMapItems.size >= 15)
        }
    }
}