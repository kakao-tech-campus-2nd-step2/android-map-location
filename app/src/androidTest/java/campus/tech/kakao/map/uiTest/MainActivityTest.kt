package campus.tech.kakao.map.uiTest

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.R
import campus.tech.kakao.map.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // 검색어를 입력하면 올바른 결과가 뜨나요?
    @Test
    fun testEnteringKeywordCorrectResult() {
        onView(withId(R.id.etKeywords)).perform(typeText("london"))

        Thread.sleep(2000)

        onView(withId(R.id.rvSearchResult))
            .check(matches(hasDescendant(withText("london1118"))))
    }

    // 결과를 클릭하면 지도 화면으로 올바르게 이동하나요?
    @Test
    fun testClickingResultsTakeMapScreenCorrectly() {
        onView(withId(R.id.etKeywords)).perform(typeText("london"))

        Thread.sleep(2000)

        onView(withText("london1118")).perform(click())

        Thread.sleep(500)

        onView(withId(R.id.mapView)).check(matches(isDisplayed()))
    }

    // 검색어 입력 후 삭제 버튼을 누르면 검색어가 잘 삭제되나요?
    @Test
    fun testKeywordDeletedByPressingDeleteButton() {
        onView(withId(R.id.etKeywords)).perform(typeText("london"))

        onView(withId(R.id.ivClear)).perform(click())

        onView(withId(R.id.etKeywords)).check(matches(withText("")))
    }

}