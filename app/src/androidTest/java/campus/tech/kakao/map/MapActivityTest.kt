import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.contrib.RecyclerViewActions
import campus.tech.kakao.map.R
import campus.tech.kakao.map.view.MapActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MapActivity::class.java)

    @Test
    fun testMapLoading() {
        onView(withId(R.id.mapView)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchButtonClick() {
        onView(withId(R.id.searchFloatingBtn)).perform(click())
        onView(withId(R.id.searchText)).perform(click(), replaceText("성심당"))
        Thread.sleep(2000)
        onView(withId(R.id.searchPlaceView)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchAndSelectPlace() {
        onView(withId(R.id.searchFloatingBtn)).perform(click())

        onView(withId(R.id.searchText)).perform(click(), replaceText("성심당"))
        Thread.sleep(2000)


        onView(withId(R.id.searchPlaceView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.clickedPlaceName)).check(matches(withText("성심당 본점")))
    }

}
