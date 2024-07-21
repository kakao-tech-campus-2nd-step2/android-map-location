package campus.tech.kakao.map.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import campus.tech.kakao.map.R


@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SearchActivity::class.java)

    @Test
    fun testEditSearchIsDisplayed() {
        onView(withId(R.id.edit_search)).check(matches(isDisplayed()))
    }

    @Test
    fun testTypingInSearchBar() {
        onView(withId(R.id.edit_search)).perform(typeText("cafe"))
        onView(withId(R.id.edit_search)).check(matches(withText("cafe")))
    }

    @Test
    fun testClickSearchResult() {
        onView(withId(R.id.edit_search)).perform(typeText("cafe"))
        Thread.sleep(3000)
        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    @Test
    fun testDeleteSavedKeyword() {
        onView(withId(R.id.edit_search)).perform(typeText("cafe"))
        Thread.sleep(3000)

        onView(withId(R.id.saved_keywords_recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.btnDelete)).perform(click())
        onView(withText("바다정원")).check(matches(isDisplayed()))
    }

    @Test
    fun testClickSavedKeywordFillsSearchBar() {
        onView(withId(R.id.edit_search)).perform(replaceText("바다정원"))
        Thread.sleep(3000)
        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.saved_keywords_recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.edit_search)).check(matches(withText("바다정원")))
    }
}
