package campus.tech.kakao.map

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testSearchTextCleared() {
        onView(withId(R.id.SearchText)).perform(typeText("Test"), closeSoftKeyboard())

        onView(withId(R.id.Delete)).perform(click())

        onView(withId(R.id.SearchText)).check(matches(withText("")))
    }

    @Test
    fun testRecyclerViewVisibility() {
        onView(withId(R.id.SearchText)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.NoSearchText)).check(matches(withText("검색 결과가 없습니다.")))
        onView(withId(R.id.RecyclerView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))

        onView(withId(R.id.SearchText)).perform(typeText("Test"), closeSoftKeyboard())
        onView(withId(R.id.NoSearchText)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        onView(withId(R.id.RecyclerView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun testInsertHistory() {

        onView(withId(R.id.SearchText)).perform(typeText("Seoul"), closeSoftKeyboard())
        onView(withId(R.id.SearchText)).perform(click())

    }
}
