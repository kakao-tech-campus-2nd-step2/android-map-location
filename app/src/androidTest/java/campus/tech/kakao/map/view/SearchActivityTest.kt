package campus.tech.kakao.map.view


import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.R
import campus.tech.kakao.map.adapter.PlaceViewAdapter
import campus.tech.kakao.map.adapter.PlaceViewHolder
import campus.tech.kakao.map.adapter.SavedPlaceViewAdapter
import campus.tech.kakao.map.model.Constants
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace
import org.hamcrest.core.AllOf.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.regex.Matcher


class SearchActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(SearchActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    // 초기 화면이 잘 구성되는지
    fun testInit() {
        onView(withId(R.id.search_result_recyclerView)).check(
            matches(isDisplayed())
        )
        onView(withId(R.id.input_search_field)).check(
            matches(isDisplayed())
        )
        onView(withId(R.id.no_search_result)).check(
            matches(isDisplayed())
        )
        onView(withId(R.id.saved_search_recyclerView)).check(
            matches(isDisplayed())
        )
    }

    @Test
    // PlaceRecyclerView가 검색어를 입력했을 때 최대 15개의 데이터를 불러오는 지 확인
    fun testPlaceRecyclerView() {

        onView(withId(R.id.input_search_field)).check(matches(isDisplayed()))
        onView(withId(R.id.search_result_recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.input_search_field)).perform(replaceText("전남대"))
        Thread.sleep(1000)

        activityRule.scenario.onActivity { activity ->
            val inputSearchField = activity.findViewById<EditText>(R.id.input_search_field)
            val placeRecyclerView =
                activity.findViewById<RecyclerView>(R.id.search_result_recyclerView)
            val itemCount = placeRecyclerView.adapter?.getItemCount() ?: 0
            assert(itemCount in 1..15)
        }
    }

    @Test
    // recyclerview의 아이템을 클릭했을 때 이동이 잘 되는지
    fun testMoveToMapActivity() {
        onView(withId(R.id.input_search_field)).check(matches(isDisplayed()))
        onView(withId(R.id.search_result_recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.input_search_field)).perform(replaceText("전남대"))
        Thread.sleep(1000)
        lateinit var place: Place

        activityRule.scenario.onActivity { activity ->
            val searchRecyclerView =
                activity.findViewById<RecyclerView>(R.id.search_result_recyclerView)
            val adapter = searchRecyclerView.adapter as PlaceViewAdapter
            place = (adapter.getPlaceAtPosition(0))
        }

        val intent = Intent()
        intent.putExtra(Constants.Keys.KEY_PLACE, place)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
        onView(withId(R.id.search_result_recyclerView)).perform(
            actionOnItemAtPosition<PlaceViewHolder>(
                0,
                click()
            )
        )

        intending(toPackage("campus.tech.kakao.map.view.MapActivity")).respondWith(result)
    }

    @Test
    // 아이템 클릭시 데이터가 저장되는지
    fun testSavePlace() {
        onView(withId(R.id.input_search_field)).check(matches(isDisplayed()))
        onView(withId(R.id.search_result_recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.input_search_field)).perform(replaceText("전남대"))
        Thread.sleep(1000)

        var existTaget: Boolean
        lateinit var place: Place
        activityRule.scenario.onActivity { activity ->
            val searchRecyclerView =
                activity.findViewById<RecyclerView>(R.id.search_result_recyclerView)
            val placeViewAdapter = searchRecyclerView.adapter as PlaceViewAdapter
            place = (placeViewAdapter.getPlaceAtPosition(0))
        }

        onView(withId(R.id.search_result_recyclerView)).perform(
            actionOnItemAtPosition<PlaceViewHolder>(0,click())
        )

        val newScenario = ActivityScenario.launch(SearchActivity::class.java)

        newScenario.onActivity { activity ->
            val savedPlaceRecyclerView =
                activity.findViewById<RecyclerView>(R.id.saved_search_recyclerView)
            val savedPlaceAdapter = savedPlaceRecyclerView.adapter as SavedPlaceViewAdapter
            existTaget = savedPlaceAdapter.existPlace(SavedPlace(place.name))
            assertTrue(existTaget)
        }
    }

    @Test
    // 삭제 버튼 클릭시 저장된 데이터가 삭제되는지
    fun testRemoveSavedPlace() {
        onView(withId(R.id.input_search_field)).check(matches(isDisplayed()))
        onView(withId(R.id.search_result_recyclerView)).check(matches(isDisplayed()))
        Thread.sleep(1000)

        var existTaget: Boolean
        lateinit var savedPlace: SavedPlace
        lateinit var savedPlaceRecyclerView : RecyclerView
        lateinit var savedPlaceAdapter : SavedPlaceViewAdapter

        activityRule.scenario.onActivity { activity ->
            savedPlaceRecyclerView =
                activity.findViewById<RecyclerView>(R.id.saved_search_recyclerView)
            savedPlaceAdapter = savedPlaceRecyclerView.adapter as SavedPlaceViewAdapter

            savedPlace = (savedPlaceAdapter.getSavedPlaceAtPosition(0))
            existTaget = savedPlaceAdapter.existPlace(savedPlace)
            assertTrue(existTaget)
        }

        onView(withId(R.id.saved_search_recyclerView)).perform(
            actionOnItemAtPosition<PlaceViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.button_saved_delete))
        )
        Thread.sleep(1000)

        activityRule.scenario.onActivity { activity ->
            existTaget = savedPlaceAdapter.existPlace(savedPlace)
            assertFalse(existTaget)
        }


    }
    object MyViewAction {
        fun clickChildViewWithId(id: Int): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): org.hamcrest.Matcher<View> {
                    return isAssignableFrom(View::class.java)
                }

                override fun getDescription(): String {
                    return "Click on a child view with specified id."
                }

                override fun perform(uiController: UiController?, view: View) {
                    val v = view.findViewById<View>(id)
                    v.performClick()
                }
            }
        }
    }
}