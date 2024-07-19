package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import campus.tech.kakao.map.presentation.MapActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

class MapActivityTest {
    lateinit var activityScenario: ActivityScenario<MapActivity>
    @Before
    fun init() {
        activityScenario = ActivityScenario.launch(MapActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun showMap() {
        onView(withId(R.id.kakao_map_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun showResultInfoOnMap() {
        onView(withId(R.id.search_box))
            .perform(click())
        onView(withId(R.id.search_box))
            .perform(click())
            .perform(replaceText("경북대"))

        // 서버에서 응답을 받을때까지 대기
        Thread.sleep(500L)

        onView(withId(R.id.search_result))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )

        onView(withId(R.id.info_sheet))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchFromHistoryAndShowResultOnMap() {
        onView(withId(R.id.search_box))
            .perform(click())
        onView(withId(R.id.search_box))
            .perform(click())
            .perform(replaceText("경북대"))

        // 서버에서 응답을 받을때까지 대기
        Thread.sleep(500L)

        onView(withId(R.id.search_result))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )
        onView(withId(R.id.search_box))
            .perform(click())
        onView(withId(R.id.search_history))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )
            .perform()

        // 서버에서 응답을 받을때까지 대기
        Thread.sleep(500L)

        onView(withId(R.id.search_result))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )
        onView(withId(R.id.info_sheet))
            .check(matches(isDisplayed()))
    }
}