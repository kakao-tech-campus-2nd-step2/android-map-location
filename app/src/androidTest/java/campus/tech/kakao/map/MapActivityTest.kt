package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.view.MapActivity
import campus.tech.kakao.map.view.SearchLocationActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MapActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testClickSearchView() {
        // when - 상단의 검색 창을 누르면
        onView(withId(R.id.searchBackgroundView)).perform(click())

        // then - 검색 activity로 이동한다
        Intents.intended(hasComponent(SearchLocationActivity::class.java.name))
    }
}