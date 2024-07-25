import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.View.MapFragment
import campus.tech.kakao.map.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapFragmentUITest {
    private lateinit var fragmentScenario: FragmentScenario<MapFragment>

    @Before
    fun setUp() {
        fragmentScenario = FragmentScenario.launchInContainer(MapFragment::class.java)
    }
    @After
    fun tearDown() {
        fragmentScenario.close()
    }
    @Test
    fun testMapViewIsDisplayed() {
        onView(withId(R.id.KakaoMapView)).check(matches(isDisplayed()))
    }
    @Test
    fun testSearchViewIsDisplayed() {
        onView(withId(R.id.searchView1)).check(matches(isDisplayed()))
    }
    @Test
    fun testSearchViewFunctionality() {
        onView(withId(R.id.searchView1)).perform(click())
         onView(withId(R.id.searchView1)).perform(typeText("검색어"), closeSoftKeyboard())
        onView(withId(R.id.searchView1)).check(matches(withText("검색어")))
    }



}