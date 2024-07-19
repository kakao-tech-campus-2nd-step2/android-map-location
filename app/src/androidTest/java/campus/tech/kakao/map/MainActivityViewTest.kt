package campus.tech.kakao.map

import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.view.MainActivity
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityViewTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        // 애플리케이션 컨텍스트로 SharedPreferences 초기화
        sharedPreferences = ApplicationProvider.getApplicationContext<android.content.Context>()
            .getSharedPreferences("PlacePreferences", android.content.Context.MODE_PRIVATE)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun testMapView() {
        // map_view가 표시되는지 확인
        onView(withId(R.id.map_view))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun testSearchEditText() {
        // searchEditText가 표시되는지 확인
        onView(withId(R.id.searchEditText))
            .check(ViewAssertions.matches(isDisplayed()))

        // searchEditText 클릭
        onView(withId(R.id.searchEditText)).perform(click())

        // SearchActivity가 시작되었는지 확인 (SearchActivity 레이아웃에 있는 요소 확인)
        onView(withId(R.id.searchEditText))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun testMarkerIsDisplayedAfterSearch() {
        // 테스트 데이터를 사용하여 SharedPreferences 준비
        val editor = sharedPreferences.edit()
        editor.putString(MainActivity.EXTRA_PLACE_LONGITUDE, "127.108621")
        editor.putString(MainActivity.EXTRA_PLACE_LATITUDE, "37.402005")
        editor.putString(MainActivity.EXTRA_PLACE_NAME, "Test Place")
        editor.putString(MainActivity.EXTRA_PLACE_ADDRESSNAME, "Test Address")
        editor.apply()

        // searchEditText 클릭하여 SearchActivity 열기
        onView(withId(R.id.searchEditText)).perform(click())

        // SearchActivity가 표시되는지 확인
        onView(withId(R.id.searchEditText))
            .check(ViewAssertions.matches(isDisplayed()))

        // SearchActivity에서 검색 작업 수행
        onView(withId(R.id.searchEditText)).perform(click()).perform(replaceText("강원대 춘천캠퍼스"))

        Thread.sleep(1000L)

        // RecyclerView에서 첫 번째 항목 선택 (이 동작이 MainActivity로 돌아가는 것으로 가정)
        onView(withId(R.id.recyclerViewSearchResults))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        Thread.sleep(3000L)

        // map_view가 표시되는지 확인 (MainActivity가 업데이트된 것을 나타냄)
        onView(withId(R.id.map_view))
            .check(ViewAssertions.matches(isDisplayed()))
    }
}