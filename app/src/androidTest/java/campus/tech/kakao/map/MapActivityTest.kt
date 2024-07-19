package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.Presenter.View.MapActivity
import campus.tech.kakao.map.Presenter.View.PlaceSearchActivity
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.hasEntry
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MapActivity::class.java)

    val mapView = onView(withId(R.id.mapView))
    val searchTextView = onView(withId(R.id.search))


    @Before
    fun init(){
        Intents.init()
    }

    @Test
    fun 카카오맵_정상작동(){
        mapView.check(matches(isDisplayed()))
    }

    @Test
    fun 검색창_클릭시_화면전환(){
        searchTextView.perform(click())
        intended(hasComponent(PlaceSearchActivity::class.java.name))
    }

    @After
    fun after(){
        Intents.release()
    }

}