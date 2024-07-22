package campus.tech.kakao.map.unitTest

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.R
import campus.tech.kakao.map.ui.MainActivity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUnitTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    // MainActivity가 잘 보이나요?
    @Test
    fun testMainActivityVisible() {
        scenario.onActivity { activity ->
            val mainView = activity.findViewById<View>(R.id.main)
            assertNotNull(mainView)
        }
    }

    // 검색 기능이 잘 동작하나요?
    @Test
    fun testVerifySearchFunction() {
        scenario.onActivity { activity ->
            val keyword = "london"
            val editText = activity.findViewById<EditText>(R.id.etKeywords)
            editText.setText(keyword)

            activity.mapViewModel.searchPlaces(keyword)

            activity.mapViewModel.searchResults.observe(activity, Observer { results ->
                assertTrue(results.isNotEmpty())
            })
        }
    }

    // 검색 결과에 따라 RecyclerView가 잘 바뀌나요?
    @Test
    fun testRecyclerViewVisibility() {
        scenario.onActivity { activity ->
            val recyclerView = activity.findViewById<RecyclerView>(R.id.rvSearchResult)
            val noResultTextView = activity.findViewById<TextView>(R.id.tvNoResults)

            activity.mapViewModel.searchResults.observe(activity, Observer { results ->
                if (results.isEmpty()) {
                    assertEquals(View.VISIBLE, noResultTextView.visibility)
                    assertEquals(View.GONE, recyclerView.visibility)
                } else {
                    assertEquals(View.GONE, noResultTextView.visibility)
                    assertEquals(View.VISIBLE, recyclerView.visibility)
                }
            })
        }
    }
}