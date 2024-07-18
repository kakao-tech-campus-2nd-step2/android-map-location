import android.content.Context
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.Model.SQLiteDb
import campus.tech.kakao.View.SearchFragment
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchFragmentUnitTest {



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
        fun testInitializeViews() {
            fragmentScenario.onFragment { fragment ->
                fragment.initializeViews(fragment.requireView())

                assertNotNull(fragment.searchView)
                assertNotNull(fragment.recyclerView)
                assertNotNull(fragment.noResultTextView)
                assertNotNull(fragment.historyRecyclerView)
            }
        }

        @Test
        fun testSetupRecyclerViews() {
            fragmentScenario.onFragment { fragment ->
                fragment.setupRecyclerViews()

                assertNotNull(fragment.recyclerView.adapter)
                assertNotNull(fragment.historyRecyclerView.adapter)
            }
        }

        @Test
        fun testUpdateHistoryData() {
            fragmentScenario.onFragment { fragment ->
                val context = ApplicationProvider.getApplicationContext<Context>()
                val databaseHelper = SQLiteDb(context)
                databaseHelper.insertIntoSelectedData("testPlace")

                fragment.updateHistoryData()

                val historyData = databaseHelper.getAllSelectedData()
                assertEquals(1, historyData.size)
                assertEquals("testPlace", historyData[0])
            }
        }

        @Test
        fun testSearchPlaces() {
            fragmentScenario.onFragment { fragment ->
                fragment.searchPlaces("testQuery")

                fragment.showNoResultMessage()
                assertEquals(View.VISIBLE, fragment.noResultTextView.visibility)
                assertEquals(View.GONE, fragment.recyclerView.visibility)
            }
        }

        @Test
        fun testShowNoResultMessage() {
            fragmentScenario.onFragment { fragment ->
                fragment.showNoResultMessage()

                assertEquals(View.VISIBLE, fragment.noResultTextView.visibility)
                assertEquals(View.GONE, fragment.recyclerView.visibility)
                assertEquals(View.GONE, fragment.historyRecyclerView.visibility)
            }
        }

        @Test
        fun testHideNoResultMessage() {
            fragmentScenario.onFragment { fragment ->
                fragment.hideNoResultMessage()

                assertEquals(View.GONE, fragment.noResultTextView.visibility)
                assertEquals(View.VISIBLE, fragment.recyclerView.visibility)
                assertEquals(View.VISIBLE, fragment.historyRecyclerView.visibility)
            }
        }

    }
