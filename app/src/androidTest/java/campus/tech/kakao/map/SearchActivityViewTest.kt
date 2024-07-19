package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.view.SearchActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityViewTest {

    private lateinit var activityScenario: ActivityScenario<SearchActivity>

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(SearchActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun testSearchEditText() {
        // 검색창이 표시되는지 확인
        onView(withId(R.id.searchEditText))
            .check(ViewAssertions.matches(isDisplayed()))

        // 검색창에 텍스트를 입력
        onView(withId(R.id.searchEditText))
            .perform(click()).perform(replaceText("강원대학교 춘천캠퍼스"), closeSoftKeyboard())

        Thread.sleep(500L)

        // 검색 결과 리사이클러뷰가 표시되는지 확인
        onView(withId(R.id.recyclerViewSearchResults))
            .check(ViewAssertions.matches(isDisplayed()))

        Thread.sleep(1000L)

        // 첫 번째 검색 결과를 클릭
        onView(withId(R.id.recyclerViewSearchResults))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        Thread.sleep(1000L)

    }

    @Test
    fun testSavedSearchesRecyclerView() {
        // 저장된 검색어 리사이클러뷰가 표시되는지 확인
        onView(withId(R.id.recyclerViewSavedSearches))
            .check(ViewAssertions.matches(isDisplayed()))
    }
}