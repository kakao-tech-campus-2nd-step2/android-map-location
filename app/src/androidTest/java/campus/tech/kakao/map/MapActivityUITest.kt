package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test


class MapActivityUITest {
	@get:Rule
	val mapActivityRule = ActivityScenarioRule(MapActivity::class.java)

	@Test
	fun 검색창_보이는지_확인(){
		val mapSearch = onView(withId(R.id.map_search))
		mapSearch.check(matches(isDisplayed()))
	}

}