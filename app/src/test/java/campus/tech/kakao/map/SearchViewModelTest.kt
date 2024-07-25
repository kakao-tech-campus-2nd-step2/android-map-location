package campus.tech.kakao.map

import android.content.Context
import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var context: Context
    private lateinit var preferenceManager: FakePreferenceManager
    private lateinit var repository: FakeRetrofitRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
        preferenceManager = FakePreferenceManager(context)
        repository = FakeRetrofitRepository()
        MapApplication.prefs = preferenceManager

        viewModel = SearchViewModel(context)
        viewModel.repository = repository
    }

    @Test
    fun getSearchHistoryList_setsSearchHistory() {
        // given
        val document = Document(
            addressName = "Address1",
            categoryGroupCode = "CategoryGroupCode1",
            categoryGroupName = "CategoryGroupName1",
            categoryName = "CategoryName1",
            distance = "Distance1",
            id = "Id1",
            phone = "Phone1",
            placeName = "Place1",
            placeUrl = "PlaceUrl1",
            roadAddressName = "RoadAddressName1",
            x = "X1",
            y = "Y1"
        )
        val searchHistory = SearchHistory("Search1", document)
        preferenceManager.addSearchHistory(searchHistory)

        val observer = Observer<List<SearchHistory>> {}
        viewModel.searchHistoryList.observeForever(observer)

        // when
        viewModel.getSearchHistoryList()

        // then
        assertEquals(1, viewModel.searchHistoryList.value!!.size)
        assertEquals("Search1", viewModel.searchHistoryList.value!![0].searchHistory)
    }

    @Test
    fun saveSearchHistory_updatesSearchHistory() {
        // given
        val document = Document(
            addressName = "Address1",
            categoryGroupCode = "CategoryGroupCode1",
            categoryGroupName = "CategoryGroupName1",
            categoryName = "CategoryName1",
            distance = "Distance1",
            id = "Id1",
            phone = "Phone1",
            placeName = "Place1",
            placeUrl = "PlaceUrl1",
            roadAddressName = "RoadAddressName1",
            x = "X1",
            y = "Y1"
        )
        val searchHistory = SearchHistory("Search1", document)

        // when
        viewModel.saveSearchHistory(searchHistory)

        // then
        assertEquals(1, preferenceManager.getArrayList(Constants.SEARCH_HISTORY_KEY).size)
        assertEquals("Search1", preferenceManager.getArrayList(Constants.SEARCH_HISTORY_KEY)[0].searchHistory)
    }

    @Test
    fun deleteSearchHistory_removesItemFromSearchHistory() {
        // given
        val document = Document(
            addressName = "Address1",
            categoryGroupCode = "CategoryGroupCode1",
            categoryGroupName = "CategoryGroupName1",
            categoryName = "CategoryName1",
            distance = "Distance1",
            id = "Id1",
            phone = "Phone1",
            placeName = "Place1",
            placeUrl = "PlaceUrl1",
            roadAddressName = "RoadAddressName1",
            x = "X1",
            y = "Y1"
        )
        val searchHistory = SearchHistory("Search1", document)
        preferenceManager.addSearchHistory(searchHistory)

        // when
        viewModel.deleteSearchHistory(0)

        // then
        assertTrue(preferenceManager.getArrayList(Constants.SEARCH_HISTORY_KEY).isEmpty())
    }

    @Test
    fun getPlace_setsLocationList() = runTest {
        // given
        val searchText = "Place"
        val documents = listOf(
            Document(
                addressName = "Address1",
                categoryGroupCode = "CategoryGroupCode1",
                categoryGroupName = "CategoryGroupName1",
                categoryName = "CategoryName1",
                distance = "Distance1",
                id = "Id1",
                phone = "Phone1",
                placeName = "Place1",
                placeUrl = "PlaceUrl1",
                roadAddressName = "RoadAddressName1",
                x = "X1",
                y = "Y1"
            )
        )
        repository.setPlaces(documents)

        // when
        viewModel.getPlace(searchText)

        // then
        advanceUntilIdle() // Wait for all coroutines to complete
        val result = viewModel.locationList.getOrAwaitValue()

        assertEquals(1, result.size)
        assertEquals("Place1", result[0].placeName)
    }
}

class FakePreferenceManager(context: Context) : PreferenceManager(context) {
    private val searchHistory = mutableListOf<SearchHistory>()

    override fun getArrayList(key: String): ArrayList<SearchHistory> {
        return ArrayList(searchHistory)
    }

    override fun savePreference(key: String, searchHistory: SearchHistory, currentList: ArrayList<SearchHistory>) {
        this.searchHistory.add(searchHistory)
    }

    override fun deleteArrayListItem(key: String, index: Int) {
        if (index >= 0 && index < searchHistory.size) {
            searchHistory.removeAt(index)
        }
    }

    fun addSearchHistory(searchHistory: SearchHistory) {
        this.searchHistory.add(searchHistory)
    }

    fun clearSearchHistory() {
        this.searchHistory.clear()
    }
}

class FakeRetrofitRepository : RetrofitRepository() {
    private var places = listOf<Document>()

    fun setPlaces(places: List<Document>) {
        this.places = places
    }

    override suspend fun getPlace(query: String): List<Document> {
        return places
    }
}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

class MainCoroutineRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}