package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.presentation.MapActivity
import campus.tech.kakao.map.presentation.SearchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MapActivity::class.java)

    @Test
    fun testActivityLaunch() {

        onView(withId(R.id.mapView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchedResultOnMap() {
        Intents.init()
        onView(withId(R.id.searchView)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(SearchActivity::class.java.name))
        Intents.release()
    }
}