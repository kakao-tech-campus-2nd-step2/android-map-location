package campus.tech.kakao.map


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.Activity.SearchActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchUITet {
    @get:Rule
    val activityRule = ActivityScenarioRule(SearchActivity::class.java)

    private val inputSpace = onView(withId(R.id.inputSpace))
    private val cancelBtn = onView(withId(R.id.cancelBtnInputSpace))
    private val mainText = onView(withId(R.id.main_text))
    private val searchRecyclerView = onView((withId(R.id.mapList)))
    private val selectRecyclerView = onView((withId(R.id.selectList)))


    @Before
    fun initSearchList() {
        inputSpace.perform(typeText("a"))
    }

    @Test
    fun inputSpaceTest() {
        inputSpace.check(matches(isDisplayed()))
    }

    @Test
    fun mainTextTest() {
        Thread.sleep(1000)
        mainText.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
    }

    @Test
    fun cancelBtnTest() {
        cancelBtn.perform(click())
        inputSpace.check(matches(withText("")))
    }

    @Test
    fun searchRecyclerViewTest() {
        Thread.sleep(1000)
        //searchRecyclerView.check(matches(hasMinimumChildCount(1)))
        searchRecyclerView.perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
    }
}