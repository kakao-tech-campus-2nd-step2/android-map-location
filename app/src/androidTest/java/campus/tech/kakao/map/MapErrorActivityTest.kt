@file:Suppress("IllegalIdentifier")
package campus.tech.kakao.map

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.activity.HomeMapActivity
import campus.tech.kakao.map.activity.MapErrorActivity
import org.junit.Rule
import org.junit.Test

class MapErrorActivityTest {
    @get:Rule
    val activityRule : ActivityScenarioRule<HomeMapActivity> =
        ActivityScenarioRule(
            Intent(
                ApplicationProvider.getApplicationContext(),
                MapErrorActivity::class.java
            ).apply {
                putExtra("errorMessage","java.lang.Exception:401")
            }
        )

    @Test
    fun `onMapError 예외 설명 에러 화면에 나타남`() {
        onView(withId(R.id.error_detail))
            .check(matches(withText("401")))
    }
}