package campus.tech.kakao.map

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.SearchLocationRepository
import campus.tech.kakao.map.view.SearchLocationActivity
import campus.tech.kakao.map.viewmodel.SearchLocationViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class SearchLocationActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SearchLocationActivity::class.java)

    private val mockRepository = mockk<SearchLocationRepository>()
    private lateinit var spyViewModel: SearchLocationViewModel

    @Before
    fun setUp() {
        every { mockRepository.getHistory() } returns listOf("기록1", "기록2")
        spyViewModel = spyk(SearchLocationViewModel(mockRepository))

        activityRule.scenario.onActivity {
            val fieldViewModel = it::class.java.getDeclaredField("viewModel")
            fieldViewModel.isAccessible = true
            fieldViewModel.set(it, spyViewModel)

            it.applyObserver()
        }

        coEvery { mockRepository.searchLocation(any()) } returns listOf(
            Location("카페1", "주소1", "카페", 37.1, 127.1),
            Location("카페2", "주소2", "카페", 37.2, 127.2)
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testInputText() {
        // when
        onView(withId(R.id.searchInputEditText)).perform(replaceText("카페"))

        // then
        onView(withId(R.id.searchInputEditText)).check(matches(withText("카페")))
    }

    @Test
    fun testRemoveText() {
        // given
        onView(withId(R.id.searchInputEditText)).perform(replaceText("카페"))

        // when
        onView(withId(R.id.removeSearchInputButton)).perform(click())

        // then
        onView(withId(R.id.searchInputEditText)).check(matches(withText("")))
    }

    @Test
    fun testSearchLocation() {
        // when
        onView(withId(R.id.searchInputEditText)).perform(replaceText("카페"))

        // then
        onView(withId(R.id.emptyResultTextView))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.searchResultRecyclerView))
            .check(matches(hasMinimumChildCount(1)))
    }

    @Test
    fun testMoveToMap() {
        // given
        every { spyViewModel.addHistory(any()) } just Runs
        onView(withId(R.id.searchInputEditText)).perform(replaceText("카페"))

        // when - 아이템을 클릭한다
        onView(withId(R.id.searchResultRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        // then - 현재 액티비티를 종료하고, 이전 액티비티(MapActivity)에 결과를 전달해야한다.
        verify { spyViewModel.addMarker(any()) }
        assertEquals(Lifecycle.State.DESTROYED, activityRule.scenario.state)
    }

    @Test
    fun testClickHistory() {
        // when
        onView(withId(R.id.searchHistoryRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                clickChildView(R.id.locationHistoryNameTextView)
            )
        )

        // then
        onView(withId(R.id.searchInputEditText)).check(matches(withText("기록1")))
    }

    private fun clickChildView(id: Int) = object : ViewAction {
        override fun getConstraints() = null
        override fun getDescription() = "Click Child View"
        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById(id))
    }
}