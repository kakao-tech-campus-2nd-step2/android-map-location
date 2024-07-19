package campus.tech.kakao.map

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.activity.MapErrorActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MapErrorActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MapErrorActivity::class.java)

    @Test
    fun 오류문구_보여주는지_확인(){
        onView(withId(R.id.error_notice))
            .check(matches(withText(R.string.error_text)))
    }
}