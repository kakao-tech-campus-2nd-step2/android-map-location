package campus.tech.kakao.map

import androidx.test.espresso.intent.Intents
import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun when_InputSearch_Expect_DisplaySearchResults() {
        //Given
        onView(withId(R.id.inputSearch)).perform(click())
        //When
        onView(withId(R.id.inputSearch)).perform(typeText("kakao"))
        SystemClock.sleep(1000)
        //Then
        onView(withId(R.id.searchRecyclerView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun when_ClickClearButton_Expect_ClearInputSearch() {
        //Given
        onView(withId(R.id.inputSearch)).perform(click())
        onView(withId(R.id.inputSearch)).perform(replaceText("kakao"))
        //When
        onView(withId(R.id.buttonX)).perform(click())
        //Then
        onView(withId(R.id.inputSearch)).check(matches(withText("")))
    }

    @Test
    fun when_SelectSearchResult_Expect_StartMapActivity() {
        //Given
        Intents.init()
        onView(withId(R.id.inputSearch)).perform(click())
        onView(withId(R.id.inputSearch)).perform(replaceText("카카오"))
        SystemClock.sleep(1000)
        //When
        onView(withId(R.id.searchRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,click()))
        //Then
        Intents.intended(IntentMatchers.hasComponent(MapActivity::class.java.name))
        Intents.release()
    }
}