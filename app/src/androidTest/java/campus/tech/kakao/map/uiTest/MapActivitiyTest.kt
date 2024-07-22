package campus.tech.kakao.map.uiTest

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.R
import campus.tech.kakao.map.ui.MapActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivitiyTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MapActivity::class.java)

    // 지도 화면이 잘 보이나요?
    @Test
    fun testMapScreenVisible() {
        onView(withId(R.id.mapView)).check(matches(isDisplayed()))
    }

    // 검색창을 클릭하면 검색 화면으로 잘 넘어가나요?
    @Test
    fun testClickingSearchBarMovesToSearchScreen() {
        onView(withId(R.id.etMapSearch)).perform(click())

        onView(withId(R.id.etKeywords)).check(matches(isDisplayed()))
    }

}