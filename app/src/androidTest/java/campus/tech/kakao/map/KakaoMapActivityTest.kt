package campus.tech.kakao.map

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.model.search.Place
import campus.tech.kakao.map.view.ActivityKeys
import campus.tech.kakao.map.view.kakaomap.KakaoMapActivity
import org.hamcrest.Matchers.not
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

    @Rule
    val activityRule = ActivityScenarioRule(KakaoMapActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(KakaoMapActivity::class.java)
    }

    @Test
    fun testKakaoMapIsLoaded() {
        onView(withId(R.id.kakaomap_err)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testGotoSearchWindowBtnClicked() {
        onView(withId(R.id.goto_search_window)).perform(click())
        onView(withId(R.id.searchWindow)).check(matches(isDisplayed()))
    }

    @Test
    fun testDisplayPlaceOnMap() {
        val place = Place(TEST_PLACE_NAME, TEST_CATEGORY_NAME, TEST_ADDRESS_NAME, TEST_X, TEST_Y)
        val intent = Intent().putExtra(ActivityKeys.INTENT_PLACE, place)

        ActivityScenario.launch<KakaoMapActivity>(intent)

        onView(withId(R.id.place_name)).check(matches(withText(TEST_PLACE_NAME)))
        onView(withId(R.id.address_name)).check(matches(withText(TEST_ADDRESS_NAME)))

        onView(withId(R.id.place_info_bottom_sheet)).check(matches(isDisplayed()))
    }

    @Test
    fun testKakaoMapErrorDisplay() {
        // 잘못된 API 전송하도록 설정
    }
}