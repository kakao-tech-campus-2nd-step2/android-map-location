package campus.tech.kakao.map.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import campus.tech.kakao.map.R

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMapViewIsDisplayed() {
        onView(withId(R.id.map_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchBarClickOpensSearchActivity() {
        onView(withId(R.id.search_bar)).perform(click())
        onView(withId(R.id.edit_search)).check(matches(isDisplayed()))
    }
}
