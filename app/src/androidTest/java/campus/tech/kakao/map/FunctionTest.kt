package campus.tech.kakao.map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class FunctionTest {

    private lateinit var scenarioMain: ActivityScenario<MainActivity>
    private lateinit var scenarioSearch: ActivityScenario<SearchActivity>
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        scenarioMain = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenarioMain.close()
        if (::scenarioSearch.isInitialized) {
            scenarioSearch.close()
        }
    }

    @Test
    fun testCompleteFlow() {
        scenarioMain.onActivity { mainActivity ->

            val searchEditText: EditText = mainActivity.findViewById(R.id.search_edit_text)
            searchEditText.performClick()

            Executors.newSingleThreadExecutor().execute {
                scenarioSearch = ActivityScenario.launch(SearchActivity::class.java)
                scenarioSearch.onActivity { searchActivity ->
                    //1 검색 기능 테스트
                    val searchEditText: EditText = searchActivity.findViewById(R.id.searchEditText)
                    searchEditText.setText("바다 정원")
                    searchActivity.performSearch("바다 정원")

                    //2 recyclerview로 검색결과 보이는지 테스트
                    val searchResults = MutableLiveData<List<MapItem>>()
                    searchResults.postValue(listOf(MapItem("1", "바다 정원", "강원도 고성군", "카페", 127.0, 37.0)))
                    searchActivity.viewModel.setSearchResults(searchResults.value ?: emptyList())

                    val searchResultsRecyclerView: RecyclerView = searchActivity.findViewById(R.id.searchResultsRecyclerView)
                    searchResultsRecyclerView.adapter?.notifyDataSetChanged()
                    assertEquals(1, searchResultsRecyclerView.adapter?.itemCount)
                }
            }
        }
    }
}