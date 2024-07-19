package campus.tech.kakao.map

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMarkerOnMap() {
        onView(withId(R.id.inputSearch)).perform(click())
        onView(withId(R.id.inputSearch)).perform(replaceText("카카오"))
        SystemClock.sleep(1000)
        onView(withId(R.id.searchRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        SystemClock.sleep(1000)
        onView(withId(R.id.bottomSheet)).check(matches(isDisplayed()))
        onView(withId(R.id.place_name)).check(matches(withText("카카오")))
        onView(withId(R.id.place_address)).check(matches(withText("경기 성남시 분당구 백현동 532")))
    }
}