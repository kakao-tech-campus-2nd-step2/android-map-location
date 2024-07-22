package campus.tech.kakao.map

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.data.PlaceDataModel
import campus.tech.kakao.map.data.PlaceDatabaseAccess
import campus.tech.kakao.map.presentation.PlaceActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class PlaceActivityTest {

    private lateinit var placeActivity: PlaceActivity
    private lateinit var mockSearchDatabaseAccess: PlaceDatabaseAccess
    private lateinit var rvPlaceList: RecyclerView
    private lateinit var rvSearchList: RecyclerView

    @Before
    fun setUp() {
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }

        val scenario = ActivityScenario.launch(PlaceActivity::class.java)
        scenario.onActivity { activity ->
            placeActivity = activity
            mockSearchDatabaseAccess = PlaceDatabaseAccess(activity, "Search.db")

            placeActivity.searchDatabaseAccess = mockSearchDatabaseAccess

            rvPlaceList = placeActivity.findViewById(R.id.rvPlaceList)
            rvSearchList = placeActivity.findViewById(R.id.rvPlaceList)
        }
    }

    @Test
    fun 검색어_저장_리스트_및_데이터베이스_속_장소_추가() {
        // 1. 장소와 빈 검색어 저장 리스트 준비
        val place = PlaceDataModel("카카오", "경기도 성남시 분당구 백현동 532", null, "37.3952969470752", "127.110449292622")
        val searchList = mutableListOf<PlaceDataModel>()

        // 2. 장소를 빈 검색어 저장 리스트에 추가
        val latch = CountDownLatch(1)
        Handler(Looper.getMainLooper()).post {
            placeActivity.addPlaceRecord(searchList, place)
            latch.countDown()
        }

        // 3. 실행될 때까지 대기
        latch.await(5, TimeUnit.SECONDS)

        // 4. 장소가 검색어 저장 리스트에 포함된 것을 확인
        assertTrue(searchList.contains(place))
        assertTrue(mockSearchDatabaseAccess.getAllPlace().contains(place))
    }

    @Test
    fun 검색어_저장_리스트_및_데이터베이스_속_장소_삭제() {
        // 1. 장소와 그 장소를 집어 넣은 검색어 저장 리스트 준비
        val place = PlaceDataModel("카카오", "경기도 성남시 분당구 백현동 532", null, "37.3952969470752", "127.110449292622")
        val searchList = mutableListOf(place)

        // 2. 검색어 저장 리스트에서 특정 장소 삭제
        val latch = CountDownLatch(1)
        Handler(Looper.getMainLooper()).post {
            placeActivity.removePlaceRecord(searchList, place)
            latch.countDown()
        }

        // 3. 실행될 때까지 대기
        latch.await(5, TimeUnit.SECONDS)

        // 4. 검색어 저장 리스트에 특정 장소가 제외된 것을 확인
        assertFalse(searchList.contains(place))
        assertFalse(mockSearchDatabaseAccess.getAllPlace().contains(place))
    }

    @Test
    fun 장소_목록_화면_제어_확인() {
        // 1. 특정 장소 목록을 가진 장소 리스트 준비
        val placeList = listOf(PlaceDataModel("카카오", "경기도 성남시 분당구 백현동 532", null, "37.3952969470752", "127.110449292622"))

        // 2. 장소 목록이 있을 때의 장소 목록 화면 설정(controlPlaceVisibility())
        val latch = CountDownLatch(1)
        Handler(Looper.getMainLooper()).post {
            placeActivity.controlPlaceVisibility(placeList)
            latch.countDown()
        }

        // 3. 실행될 때까지 대기
        latch.await(5, TimeUnit.SECONDS)

        // 4. 장소 목록이 보이는 지 확인
        assertEquals(View.VISIBLE, rvPlaceList.visibility)
    }

    @Test
    fun 검색어_저장_목록_화면_제어_확인() {
        // 1. 특정 저장된 검색어 기록이 있는 검색 리스트 준비
        val searchList = listOf(PlaceDataModel("카카오", "경기도 성남시 분당구 백현동 532", null, "37.3952969470752", "127.110449292622"))

        // 2. 검색어 저장 목록이 있을 때의 검색어 목록 화면 설정(controlSearchVisibility())
        val latch = CountDownLatch(1)
        Handler(Looper.getMainLooper()).post {
            placeActivity.controlPlaceVisibility(searchList)
            latch.countDown()
        }

        // 3. 실행될 때까지 대기
        latch.await(5, TimeUnit.SECONDS)

        // 4. 검색어 목록이 보이는 지 확인
        assertEquals(View.VISIBLE, rvSearchList.visibility)
    }
}
