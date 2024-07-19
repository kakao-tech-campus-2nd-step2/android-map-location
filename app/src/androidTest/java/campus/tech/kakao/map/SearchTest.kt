package campus.tech.kakao.map

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class SearchTest {
    @Test
    fun testSearchFunctionality() {
        val scenario = ActivityScenario.launch(SearchActivity::class.java)

        onView(withId(R.id.searchWord))
            .check(matches(isDisplayed()))

        onView(withId(R.id.searchWord))
            .perform(replaceText("카페"))

        scenario.close()
    }

}