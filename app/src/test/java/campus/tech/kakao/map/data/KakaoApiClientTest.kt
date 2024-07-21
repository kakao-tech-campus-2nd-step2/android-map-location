package campus.tech.kakao.map.data

import org.junit.Assert.assertNotNull
import org.junit.Test

class KakaoApiClientTest {

    @Test
    fun testCreateService() {
        val service = KakaoApiClient.createService()
        assertNotNull(service)
    }
}
