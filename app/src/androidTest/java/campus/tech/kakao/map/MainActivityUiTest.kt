package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.view.map.MapActivity
import campus.tech.kakao.map.view.search.LocationAdapter
import campus.tech.kakao.map.view.search.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {
    @get: Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun `검색창에_검색어를_입력하고_clearButton을_클릭하면_입력한_검색어가_삭제된다`() {
        onView(withId(R.id.SearchEditTextInMain)).perform(replaceText("카페")).check(matches(withText("카페")))
        onView(withId(R.id.clearButton)).perform(click())
        onView(withId(R.id.SearchEditTextInMain)).check(matches(withText("")))
    }

    @Test
    fun `리싸이클러뷰가_화면에_표시된다`() {
        onView(withId(R.id.locationRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun `검색창에_부산대를_입력하고_리싸이클러뷰의_0번_아이템을_클릭하면_지도와_BottomSheet가_화면에_표시된다`() {
        Intents.init()
        onView(withId(R.id.SearchEditTextInMain)).perform(replaceText("부산대"))
        Thread.sleep(3000)
        onView(withId(R.id.locationRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        intended(hasExtra("title", "부산대학교 부산캠퍼스"))
        intended(hasComponent(MapActivity::class.java.name))
        onView(withId(R.id.bottom_sheet_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.map_view)).check(matches(isDisplayed()))
    }
}