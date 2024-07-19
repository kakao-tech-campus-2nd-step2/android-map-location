package campus.tech.kakao.map

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.any
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matcher
import org.junit.runner.Description


@RunWith(AndroidJUnit4::class)
@MediumTest
class SearchTest {
    @Test
    fun testSearchFunctionality() {
        val scenario = ActivityScenario.launch(SearchActivity::class.java)

        onView(withId(R.id.searchWord))
            .check(matches(isDisplayed()))

        Thread.sleep(2000)

        onView(withId(R.id.searchWord))
            .perform(replaceText("카페"))

        Thread.sleep(2000)

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )

        Thread.sleep(2000)

        scenario.close()
    }

}