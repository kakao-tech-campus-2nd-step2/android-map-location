package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.model.search.Place
import campus.tech.kakao.map.view.ActivityKeys
import campus.tech.kakao.map.view.search.SearchWindowActivity
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_SEARCH_KEYWORD = "충남대학교"
private const val TEST_PLACE_NAME = "충남대학교 대덕캠퍼스"

@RunWith(AndroidJUnit4::class)
class SearchWindowTest {
    @Rule
    val activityRule = ActivityScenarioRule(SearchWindowActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(SearchWindowActivity::class.java)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testSearchWindowTextChange() {
        onView(withId(R.id.searchWindow)).perform(
            typeText(TEST_SEARCH_KEYWORD),
            closeSoftKeyboard()
        )
        onView(withId(R.id.emptySearchResults)).check(matches(not(isDisplayed())))
        onView(withId(R.id.searchResultsList)).check(matches(isDisplayed()))
    }

    @Test
    fun testDeleteSearchKeyword() {
        onView(withId(R.id.delSearchKeyword)).perform(click())
        onView(withId(R.id.searchWindow)).check(matches(withText("")))
    }

    @Test
    fun testSearchResultClick() {
        onView(withId(R.id.searchWindow)).perform(
            typeText(TEST_SEARCH_KEYWORD),
            closeSoftKeyboard()
        )
        onView(withId(R.id.searchResultsList))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Intents.intended(hasExtra(ActivityKeys.INTENT_PLACE, Place::class.java))
    }

    @Test
    fun testSavedSearchKeywordClick() {
        // recyclerview의 item view 클릭 추가
        // onView(withId(R.id.searchWindow)).check(matches(withText(TEST_PLACE_NAME)))
    }

    @Test
    fun testSavedSearchKeywordDelete() {
        // recyclerview의 item view 클릭 추가
        // onView(withText(TEST_PLACE_NAME)).check(matches(not(isDisplayed())))
    }
}