package campus.tech.kakao.map.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import org.junit.Before
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import campus.tech.kakao.map.R

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val idlingResource = CountingIdlingResource("RecyclerViewIdlingResource")

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun testMapViewIsDisplayed() {
        onView(withId(R.id.map_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchBarClickOpensSearchActivity() {
        onView(withId(R.id.search_bar)).perform(click())
        onView(withId(R.id.edit_search)).check(matches(isDisplayed()))
    }

    @Test
    fun testMarkerIsDisplayed() {
        onView(withId(R.id.search_bar)).perform(click())
        onView(withId(R.id.edit_search)).perform(replaceText("바다 정원"), closeSoftKeyboard())

        idlingResource.increment()

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )
    }

    @Test
    fun testBottomSheetIsDisplayedWithCorrectInfo() {
        onView(withId(R.id.search_bar)).perform(click())
        onView(withId(R.id.edit_search)).perform(replaceText("바다정원"))
        Thread.sleep(3000)
        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.bottom_sheet)).check(matches(withText("바다정원")))
        onView(withId(R.id.place_name)).check(matches(withText("강원 고성군 토성면 버리깨길 23")))
        onView(withId(R.id.bottom_sheet)).check(matches(isDisplayed()))
    }
}
