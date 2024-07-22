package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityTest {
    @get:Rule
    val mapActivity = ActivityScenarioRule(MapActivity::class.java)

    @Test
    fun 지도화면에서_검색창을_누르면_목록화면으로_이동한다() {
        Intents.init()
        onView(withId(R.id.etSearch))
            .perform(click())
        Intents.intended(hasComponent(MainActivity::class.java.name))
        Intents.release()
    }

}