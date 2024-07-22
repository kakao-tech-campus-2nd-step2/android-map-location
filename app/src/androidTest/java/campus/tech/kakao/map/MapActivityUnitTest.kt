package campus.tech.kakao.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import campus.tech.kakao.map.ui.MapActivity
import campus.tech.kakao.map.ui.ModalBottomSheet
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityUnitTest {

    @get:Rule
    var activityRule = ActivityTestRule(MapActivity::class.java, true, false)

    private lateinit var mapActivity: MapActivity

    @Before
    fun setUp() {
        // 1. 전달할 데이터 담기
        val intent = Intent().apply {
            putExtra("name", "카카오")
            putExtra("address", "경기도 성남시 분당구 백현동 532")
            putExtra("latitude", "37.3952969470752")
            putExtra("longitude", "127.110449292622")
        }

        // 2. intent 포함한 채 시작
        mapActivity = activityRule.launchActivity(intent)
    }

    @Test
    fun 인텐트_전달시_값_전달_확인() {
        // 1. onMapReady()가 호출될 때까지 대기
        Thread.sleep(2000) // Wait for async operations to complete

        // 2. intent를 통해 받은 데이터 값 확인
        assertEquals("37.3952969470752", mapActivity.latitude)
        assertEquals("127.110449292622", mapActivity.longitude)
    }

    @Test
    fun 인텐트_전달시_모달창_동작_확인() {
        // 1. onMapReady()가 호출될 때까지 대기
        Thread.sleep(2000) // Wait for async operations to complete

        // 2. BottomSheet에 intent로 전달한 정보 제대로 로딩되는지 확인
        val fragment = mapActivity.supportFragmentManager.findFragmentByTag("modalBottomSheet") as ModalBottomSheet?
        assertNotNull(fragment)
        assertEquals("카카오", fragment?.arguments?.getString("name"))
        assertEquals("경기도 성남시 분당구 백현동 532", fragment?.arguments?.getString("address"))
    }

    @Test
    fun 마지막_위치_데이터_저장_확인() {
        // 1. sharedPreferences를 통해 마지막 위치 저장
        mapActivity.saveLocation()

        // 2. sharedPreferences를 통해 저장된 위치 받아오기
        val sharedPreferences = mapActivity.getSharedPreferences("locationInfo", AppCompatActivity.MODE_PRIVATE)
        val savedLatitude = sharedPreferences.getString("latitude", null)
        val savedLongitude = sharedPreferences.getString("longitude", null)

        // 3. 받아온 데이터 확인
        assertEquals("37.3952969470752", savedLatitude)
        assertEquals("127.110449292622", savedLongitude)
    }
}
