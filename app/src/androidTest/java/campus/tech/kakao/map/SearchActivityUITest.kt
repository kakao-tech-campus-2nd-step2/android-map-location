package campus.tech.kakao.map

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.adapter.DocumentAdapter
import okhttp3.internal.wait
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class SearchActivityUITest {
	@get:Rule
	val searchActivityRule = ActivityScenarioRule(SearchActivity::class.java)


	@Test
	fun 입력한_검색어_X_눌러_삭제() {
		val query = "박물관"
		val search = onView(allOf(withId(R.id.search), withParent(withId(R.id.search_main))))
		val clear = onView(withId(R.id.search_clear))
		search.perform(replaceText(query))
		clear.perform(click())
		search.check(matches(withText("")))
	}

	@Test
	fun 검색_결과_클릭시(){
		val query = "박물관"
		val search = onView(allOf(withId(R.id.search), withParent(withId(R.id.search_main))))
		search.perform(replaceText(query))
		val searchResult = onView(withId(R.id.search_result_recycler_view))
		searchResult.check(matches(isDisplayed()))
	}

}