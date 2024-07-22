@file:Suppress("IllegalIdentifier")
package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.activity.DataSearchActivity
import org.junit.Rule
import org.junit.Test


class DataSearchActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(DataSearchActivity::class.java)

    @Test
    fun `검색어 입력시 검색 결과가 나타남`() {
        onView(withId(R.id.searchBar))
            .perform(replaceText("전남대학교"), closeSoftKeyboard())
        onView(withId(R.id.searchResulListView))
            .check(matches(isDisplayed()))
    }
}