package campus.tech.kakao.map

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class MapUITest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MapActivity::class.java)

    private val inputSpace = Espresso.onView(ViewMatchers.withId(R.id.toSearchActivity))
    private val mapView = Espresso.onView(ViewMatchers.withId(R.id.mapView))

    @Test
    fun inputSpaceTest() {
        inputSpace.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun mapViewTest() {
        mapView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}