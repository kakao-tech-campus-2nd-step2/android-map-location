package campus.tech.kakao.map.ui.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {
    @get:Rule
    val rule = ActivityScenarioRule(SearchActivity::class.java)

    @Test
    fun testClearButton() {
        onView(withId(R.id.search_edit_text)).perform(replaceText("안녕"))
        // clearImageView를 클릭하고 검색 EditText가 비어 있는지 확인.
        onView(withId(R.id.search_clear_image_view)).perform(click())
        onView(withId(R.id.search_edit_text)).check(matches(withText("")))
    }

    @Test
    fun testDisplaySearchResult() {
        // 검색어 입력
        onView(withId(R.id.search_edit_text)).perform(replaceText("대형마트"))

        // 결과가 나올 수 있게 잠깐 대기
        Thread.sleep(2000)

        // 리사이클러뷰가 나오는지 확인
        onView(withId(R.id.search_result_recycler_view)).check(matches(isDisplayed()))
    }
}
