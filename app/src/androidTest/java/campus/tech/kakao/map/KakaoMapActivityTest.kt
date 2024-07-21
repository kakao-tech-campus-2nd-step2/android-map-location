package campus.tech.kakao.map

import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import campus.tech.kakao.map.model.search.Place
import campus.tech.kakao.map.view.ActivityKeys
import campus.tech.kakao.map.view.kakaomap.KakaoMapActivity
import kotlinx.coroutines.delay
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_PLACE_NAME = "Test Place"
private const val TEST_CATEGORY_NAME = "Test Category"
private const val TEST_ADDRESS_NAME = "Test Address"
private const val TEST_X = "36.37003"
private const val TEST_Y = "127.34594"

@RunWith(AndroidJUnit4::class)
class KakaoMapActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(KakaoMapActivity::class.java)

    private lateinit var mDvice: UiDevice

    @Before
    fun setUp() {
        ActivityScenario.launch(KakaoMapActivity::class.java)
        mDvice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun testKakaoMapIsLoaded() {
        // then
        onView(withId(R.id.kakaomap_err)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testDisplaySearchWindow() {
        // when
        onView(withId(R.id.goto_search_window)).perform(click())

        // then
        onView(withId(R.id.searchWindow)).check(matches(isDisplayed()))
    }

    @Test
    fun testDisplayPlaceOnMap() {
        // given
        val place = Place(TEST_PLACE_NAME, TEST_CATEGORY_NAME, TEST_ADDRESS_NAME, TEST_X, TEST_Y)
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            KakaoMapActivity::class.java
        ).apply {
            putExtra(ActivityKeys.INTENT_PLACE, place)
        }

        // when
        ActivityScenario.launch<KakaoMapActivity>(intent).use {
            mDvice.waitForIdle(3000)

            // then
            onView(withId(R.id.place_info_bottom_sheet)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testDisplayKakaoMapError() {
        // 잘못된 API 전송하도록 설정
        // onView(withId(R.id.kakaomap_err)).check(matches(isDisplayed()))
    }
}