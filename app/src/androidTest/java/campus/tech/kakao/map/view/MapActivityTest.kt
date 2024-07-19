package campus.tech.kakao.map.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents

import org.junit.Test
import campus.tech.kakao.map.R
import org.junit.After
import org.junit.Before
import org.junit.Rule


class MapActivityTest{


    @get:Rule
    val activityRule = ActivityScenarioRule(MapActivity::class.java)

    private val idlingResource = CountingIdlingResource("Load")

    @Before
    fun setUp() {
        Intents.init()
        testInit()
    }

    @After
    fun tearDown() {
        Intents.release()
    }
    @Test
    // 초기 화면이 잘 구성되는지
    fun testInit() {
        onView(withId(R.id.input_search_field)).check(
            matches(isDisplayed()))
        onView(withId(R.id.search_icon)).check(
            matches(isDisplayed()))
        onView(withId(campus.tech.kakao.map.R.id.error_text)).check(
            matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.map_view)).check(
            matches(isDisplayed()))
    }

    @Test
    // 오류가 났을 때 오류화면이 잘 띄워지는지
    fun testError(){
        activityRule.scenario.onActivity { activity ->
            activity.showErrorMessageView("인증 과정 중 원인을 알 수 없는 에러가 발생했습니다")

        }
        onView(withId(R.id.error_text)).check(matches(isDisplayed()))
    }

    @Test
    // 액티비티가 잘 이동하는지
    fun testMoveSearchActivity(){
        onView(withId(R.id.input_search_field)).perform(click())
        intended(hasComponent("campus.tech.kakao.map.view.SearchActivity"))
    }
}
