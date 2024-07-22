package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MapActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun testFirstScreen() {
        onView(withId(R.id.search_button)).check(ViewAssertions.matches(isDisplayed()))
    }
    @Test
    fun testSearchButtonClick() {
        onView(withId(R.id.search_button)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.search_edit_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
