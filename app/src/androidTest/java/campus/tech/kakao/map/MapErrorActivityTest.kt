package campus.tech.kakao.map

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.view.MapErrorActivity
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class MapErrorActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(MapErrorActivity::class.java)

    @Test
    fun testClickRefreshButton() {
        // when
        onView(withId(R.id.refreshBackgroundView)).perform(click())

        // then
        assertEquals(Lifecycle.State.DESTROYED, activityRule.scenario.state)
    }
}