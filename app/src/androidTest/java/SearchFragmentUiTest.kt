import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.View.SearchFragment
import campus.tech.kakao.map.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)

class SearchFragmentUiTest {
    private lateinit var fragmentScenario: FragmentScenario<SearchFragment>
    @Before
    fun setUp() {
        fragmentScenario = FragmentScenario.launchInContainer(SearchFragment::class.java)
    }
    @After
    fun tearDown() {
        fragmentScenario.close()
    }
    @Test
    fun testSearchViewIsDisplayed() {
        onView(withId(R.id.searchView2)).check(matches(isDisplayed()))
    }
    @Test
    fun testRecyclerViewIsDisplayed() {

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }
    @Test
    fun testHistoryRecyclerViewIsDisplayed() {
        onView(withId(R.id.historyRecyclerView)).check(matches(isDisplayed()))
    }
    @Test
    fun testSearchViewFunctionality() {
        onView(withId(R.id.searchView2)).perform(click())
        onView(withId(R.id.searchView2)).perform(typeText("검색어"))


        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }


}