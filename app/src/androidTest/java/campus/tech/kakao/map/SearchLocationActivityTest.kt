package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.view.SearchLocationActivity
import org.junit.Rule
import org.junit.Test

class SearchLocationActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SearchLocationActivity::class.java)

    @Test
    fun testInputText() {
        onView(withId(R.id.searchInputEditText)).perform(replaceText("카페"))
        onView(withId(R.id.searchInputEditText)).check(matches(withText("카페")))
    }

    @Test
    fun testRemoveText() {
        onView(withId(R.id.searchInputEditText)).perform(replaceText("카페"))
        onView(withId(R.id.removeSearchInputButton)).perform(click())
        onView(withId(R.id.searchInputEditText)).check(matches(withText("")))
    }

    @Test
    fun testSearchLocation() {
        onView(withId(R.id.searchInputEditText)).perform(replaceText("카페"))
        Thread.sleep(500)
        onView(withId(R.id.emptyResultTextView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.searchResultRecyclerView)).check(matches(hasMinimumChildCount(1)))
    }
}