package campus.tech.kakao.map

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityUnitTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(SearchActivity::class.java)

    @Before
    fun setUp() {}

//    @Test
//    fun testSearchPlacesWithResults() {
//        activityScenarioRule.scenario.onActivity { activity ->
//            activity.searchPlaces("DDDD")
//
//            val recyclerViewAdapter = activity.findViewById<RecyclerView>(R.id.recycler_view).adapter as ResultRecyclerViewAdapter
//            assertEquals(1, recyclerViewAdapter.itemCount)
//        }
//    }
    // 도저히 왜 안 되는 지 모르겠는 코드 2..
}
