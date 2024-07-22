package campus.tech.kakao.map.ui.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.R
import campus.tech.kakao.map.ui.search.SearchActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MapActivity::class.java)

    @Test
    fun testNavigateToSearchActivity() {
        // Espresso Intents 초기화
        Intents.init()

        try {
            Thread.sleep(1000)
            // Search Box 클릭
            onView(withId(R.id.search_box_layout)).perform(click())

            // SearchActivity로의 인텐트가 올바르게 발생했는지 확인
            intended(hasComponent(SearchActivity::class.java.name))

            onView(withId(R.id.search_edit_text)).perform(ViewActions.replaceText("대형마트"))

            // 결과가 나올 수 있게 잠깐 대기
            Thread.sleep(1500)

            // 리사이클러뷰가 나오는지 확인
            onView(withId(R.id.search_result_recycler_view)).check(
                matches(
                    isDisplayed(),
                ),
            )

            // 2번째 항목을 클릭
            onView(withId(R.id.search_result_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    click(),
                ),
            )

            // 잠깐 대기
            Thread.sleep(1000)
            onView(withId(R.id.map_bottom_sheet_layout)).check(matches(isDisplayed()))

            // Search Box 클릭
            onView(withId(R.id.search_box_layout)).perform(click())
            onView(
                Matchers.allOf(
                    withId(R.id.saved_search_word_text_view),
                    ViewMatchers.isDescendantOfA(
                        withId(R.id.saved_search_word_recycler_view),
                    ),
                ),
            ).perform(click())
            // 잠깐 대기
            Thread.sleep(2000)
            onView(withId(R.id.map_bottom_sheet_layout)).check(matches(isDisplayed()))
        } finally {
            // Espresso Intents 해제
            Intents.release()
        }
    }

    @Test
    fun testApiKeyErrorHandling() {
        Intents.init()
        try {
            // 키 값을 잘못 넣었을 때 OnMapError 발생 시 Error Activity로 넘어가는지 테스트
            Thread.sleep(1000)
            onView(withId(R.id.error_message_text_view)).check(matches(isDisplayed()))
        } finally {
            Intents.release()
        }
    }
}
