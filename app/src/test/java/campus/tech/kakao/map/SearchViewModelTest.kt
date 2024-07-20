//package campus.tech.kakao.map
//
//import android.content.Context
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.Observer
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import kotlinx.coroutines.test.UnconfinedTestDispatcher
//import kotlinx.coroutines.test.advanceUntilIdle
//import kotlinx.coroutines.test.runTest
//import org.junit.*
//import org.junit.Assert.*
//import org.junit.runner.RunWith
//import org.robolectric.annotation.Config
//
//@RunWith(AndroidJUnit4::class)
//@Config(manifest = Config.NONE)
//class SearchViewModelTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var viewModel: SearchViewModel
//    private lateinit var context: Context
//    private lateinit var preferenceManager: FakePreferenceManager
//    private lateinit var repository: FakeRetrofitRepository
//
//    @Before
//    fun setUp() {
//        context = ApplicationProvider.getApplicationContext<Context>()
//        preferenceManager = FakePreferenceManager(context)
//        repository = FakeRetrofitRepository()
//        MapApplication.prefs = preferenceManager
//
//        viewModel = SearchViewModel(context)
//        viewModel.repository = repository  // repository를 설정합니다.
//    }
//
//    @Test
//    fun getSearchHistoryList_setsSearchHistory() {
//        val document = Document(
//            addressName = "Address1",
//            categoryGroupCode = "CategoryGroupCode1",
//            categoryGroupName = "CategoryGroupName1",
//            categoryName = "CategoryName1",
//            distance = "Distance1",
//            id = "Id1",
//            phone = "Phone1",
//            placeName = "Place1",
//            placeUrl = "PlaceUrl1",
//            roadAddressName = "RoadAddressName1",
//            x = "X1",
//            y = "Y1"
//        )
//        val searchHistory = SearchHistory("Search1", document)
//        preferenceManager.addSearchHistory(searchHistory)
//
//        val observer = Observer<List<SearchHistory>> {}
//        viewModel.searchHistoryList.observeForever(observer)
//
//        viewModel.getSearchHistoryList()
//        assertEquals(1, viewModel.searchHistoryList.value!!.size)
//        assertEquals("Search1", viewModel.searchHistoryList.value!![0].searchHistory)
//    }
//
//    @Test
//    fun saveSearchHistory_updatesSearchHistory() {
//        val document = Document(
//            addressName = "Address1",
//            categoryGroupCode = "CategoryGroupCode1",
//            categoryGroupName = "CategoryGroupName1",
//            categoryName = "CategoryName1",
//            distance = "Distance1",
//            id = "Id1",
//            phone = "Phone1",
//            placeName = "Place1",
//            placeUrl = "PlaceUrl1",
//            roadAddressName = "RoadAddressName1",
//            x = "X1",
//            y = "Y1"
//        )
//        val searchHistory = SearchHistory("Search1", document)
//
//        viewModel.saveSearchHistory(searchHistory)
//
//        assertEquals(1, preferenceManager.getArrayList(Constants.SEARCH_HISTORY_KEY).size)
//        assertEquals("Search1", preferenceManager.getArrayList(Constants.SEARCH_HISTORY_KEY)[0].searchHistory)
//    }
//
//    @Test
//    fun deleteSearchHistory_removesItemFromSearchHistory() {
//        val document = Document(
//            addressName = "Address1",
//            categoryGroupCode = "CategoryGroupCode1",
//            categoryGroupName = "CategoryGroupName1",
//            categoryName = "CategoryName1",
//            distance = "Distance1",
//            id = "Id1",
//            phone = "Phone1",
//            placeName = "Place1",
//            placeUrl = "PlaceUrl1",
//            roadAddressName = "RoadAddressName1",
//            x = "X1",
//            y = "Y1"
//        )
//        val searchHistory = SearchHistory("Search1", document)
//        preferenceManager.addSearchHistory(searchHistory)
//
//        viewModel.deleteSearchHistory(0)
//
//        assertTrue(preferenceManager.getArrayList(Constants.SEARCH_HISTORY_KEY).isEmpty())
//    }
//
//    @Test
//    fun getPlace_setsLocationList() = runTest(UnconfinedTestDispatcher()) {
//        val searchText = "Place"
//        val documents = listOf(
//            Document(
//                addressName = "Address1",
//                categoryGroupCode = "CategoryGroupCode1",
//                categoryGroupName = "CategoryGroupName1",
//                categoryName = "CategoryName1",
//                distance = "Distance1",
//                id = "Id1",
//                phone = "Phone1",
//                placeName = "Place1",
//                placeUrl = "PlaceUrl1",
//                roadAddressName = "RoadAddressName1",
//                x = "X1",
//                y = "Y1"
//            )
//        )
//        repository.setPlaces(documents)
//
//        val observer = Observer<List<Document>> {}
//        viewModel.locationList.observeForever(observer)
//
//        viewModel.getPlace(searchText)
//        advanceUntilIdle()
//
//        assertEquals(1, viewModel.locationList.value!!.size)
//        assertEquals("Place1", viewModel.locationList.value!![0].placeName)
//    }
//}
//
//class FakePreferenceManager(context: Context) : PreferenceManager(context) {
//    private val searchHistory = mutableListOf<SearchHistory>()
//
//    override fun getArrayList(key: String): ArrayList<SearchHistory> {
//        return ArrayList(searchHistory)
//    }
//
//    override fun savePreference(key: String, searchHistory: SearchHistory, currentList: ArrayList<SearchHistory>) {
//        this.searchHistory.add(searchHistory)
//    }
//
//    override fun deleteArrayListItem(key: String, index: Int) {
//        if (index >= 0 && index < searchHistory.size) {
//            searchHistory.removeAt(index)
//        }
//    }
//
//    fun addSearchHistory(searchHistory: SearchHistory) {
//        this.searchHistory.add(searchHistory)
//    }
//
//    fun clearSearchHistory() {
//        this.searchHistory.clear()
//    }
//}
//
//class FakeRetrofitRepository : RetrofitRepository() {
//    private var places = listOf<Document>()
//
//    fun setPlaces(places: List<Document>) {
//        this.places = places
//    }
//
//    override suspend fun getPlace(query: String): List<Document> {
//        return places
//    }
//}
