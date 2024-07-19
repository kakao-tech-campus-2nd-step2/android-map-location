package campus.tech.kakao.map

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.contrib.RecyclerViewActions
import campus.tech.kakao.map.presentation.MapActivity
import campus.tech.kakao.map.presentation.PlaceActivity
import campus.tech.kakao.map.presentation.PlaceAdapter
import campus.tech.kakao.map.presentation.SearchHistoryAdapter
import io.mockk.verify
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

class PlaceActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(PlaceActivity::class.java)

    @Test
    fun 검색박스에_텍스트_입력_시_검색_결과가_표시되는지_테스트() {
        onView(withId(R.id.searchEditText)).perform(typeText("N"))
        Thread.sleep(3000)
        onView(withId(R.id.placeRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun 검색_아이템_클릭_시_히스토리에_잘_저장되는지_테스트() {
        onView(withId(R.id.searchEditText)).perform(typeText("N"))
        Thread.sleep(2000)
        onView(withId(R.id.placeRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PlaceAdapter.PlaceViewHolder>(0, click())
        )
        Espresso.pressBack()
        onView(withId(R.id.historyRecyclerView)).check(matches(hasDescendant(withText("N"))))
    }

    @Test
    fun 검색_결과_클릭_시_지도_액티비티로_넘어가는지_테스트() {
        Intents.init()
        onView(withId(R.id.searchEditText)).perform(typeText("N"))
        Thread.sleep(1000)
        onView(withId(R.id.placeRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PlaceAdapter.PlaceViewHolder>(0, click())
        )
        Intents.intended(hasComponent(MapActivity::class.java.name))

        Intents.release()
    }

    @Test
    fun 검색_히스토리_항목_클릭_시_검색_결과_표시되는지_테스트() {
        onView(withId(R.id.searchEditText)).perform(typeText("Z"))
        Thread.sleep(3000)
        onView(withId(R.id.placeRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PlaceAdapter.PlaceViewHolder>(0, click())
        )
        Espresso.pressBack()
        onView(withId(R.id.historyRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SearchHistoryAdapter.HistoryViewHolder>(
                0,
                click()
            )
        )
        Thread.sleep(3000)
        onView(withId(R.id.placeRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun 검색_히스토리에서_삭제_시_히스토리에서_제거되는지_테스트() {
        onView(withId(R.id.searchEditText)).perform(typeText("N"))
        Thread.sleep(1000)
        onView(withId(R.id.historyRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SearchHistoryAdapter.HistoryViewHolder>(
                0,
                click()
            )
        )
        Thread.sleep(1000)
        onView(withId(R.id.historyRecyclerView)).check(matches(not(hasDescendant(withText("N")))))
    }

    @Test
    fun 검색박스에서_텍스트_지우면_검색_결과가_사라지고_빈_메시지_표시되는지_테스트() {
        onView(withId(R.id.searchEditText)).perform(typeText("N"))
        onView(withId(R.id.cancelButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.emptyMessage)).check(matches(isDisplayed()))
        onView(withId(R.id.placeRecyclerView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun 검색박스에서_x_버튼누르면_텍스트_지워지는지_테스트() {
        onView(withId(R.id.searchEditText)).perform(typeText("N"))
        onView(withId(R.id.cancelButton)).perform(click())
        onView(withId(R.id.searchEditText)).check(matches(withText("")))
    }

    @Test
    fun 기기_회전_후에_검색_히스토리가_유지되는지_테스트() {
        onView(withId(R.id.searchEditText)).perform(typeText("N"))
        activityRule.scenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        onView(withId(R.id.historyRecyclerView)).check(matches(hasDescendant(withText("N"))))
    }

    @Test
    fun 검색_결과가_스크롤_가능한지_테스트() {
        onView(withId(R.id.searchEditText)).perform(typeText("N"))
        Thread.sleep(3000)
        onView(withId(R.id.placeRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<PlaceAdapter.PlaceViewHolder>(10)
        )
        onView(withId(R.id.placeRecyclerView)).check(matches(isDisplayed()))
    }

}
