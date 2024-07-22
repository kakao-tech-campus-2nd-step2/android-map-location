package campus.tech.kakao.map

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import campus.tech.kakao.map.presentation.MapActivity
import campus.tech.kakao.map.presentation.PlaceActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityUITest {

    @get:Rule
    val activityRule = ActivityTestRule(MapActivity::class.java)

    @Test
    fun 지도_정상_작동_여부_확인() {
        // 1. MapView(mapView)가 표시되고 있는지 확인
        Espresso.onView(ViewMatchers.withId(R.id.mapView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // 2. 지도 화면에 검색어 입력창(etSearch)이 표시되는지 확인
        Espresso.onView(ViewMatchers.withId(R.id.etSearch))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun 목록_선택시_화면_전환_여부_확인() {
        Intents.init()

        try {
            // 1. 검색어 입력창(etSearch)을 선택하기
            Espresso.onView(ViewMatchers.withId(R.id.etSearch))
                .perform(ViewActions.click())

            // 2. 다음 화면(PlaceActivity)으로의 인텐트가 시작되었는지 확인
            Intents.intended(IntentMatchers.hasComponent(PlaceActivity::class.java.name))


        } finally {
            Intents.release()
        }
    }
}