package campus.tech.kakao.map

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.kakao.vectormap.LatLng
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config@RunWith(RobolectricTestRunner::class)

@Config(sdk = [28])
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var preferenceManager: FakePreferenceManager

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        preferenceManager = FakePreferenceManager(context)
        MapApplication.prefs = preferenceManager
        viewModel = MainViewModel(preferenceManager)
    }

    @Test
    fun setLocation_withCoordinates_returnsLatLng() {
        val latitude = 37.7749
        val longitude = -122.4194
        val result = viewModel.setLocation(latitude, longitude)
        val expected = LatLng.from(latitude, longitude)

        assertEquals(expected.latitude, result?.latitude)
        assertEquals(expected.longitude, result?.longitude)
    }

    @Test
    fun setLocation_withoutCoordinates_returnsLastKnownLatLng() {
        val document = Document(
            addressName = "123 Main St",
            categoryGroupCode = "ABC",
            categoryGroupName = "Category Group",
            categoryName = "Category",
            distance = "10",
            id = "1",
            phone = "123-456-7890",
            placeName = "Test Place",
            placeUrl = "http://example.com",
            roadAddressName = "456 Road St",
            x = "-122.4194",
            y = "37.7749"
        )
        val searchHistory = SearchHistory("Some history", document)
        preferenceManager.addSearchHistory(searchHistory)

        val result = viewModel.setLocation()
        val expected = LatLng.from(37.7749, -122.4194)

        assertEquals(expected.latitude, result?.latitude)
        assertEquals(expected.longitude, result?.longitude)
    }

    // FakePreferenceManager 클래스 추가
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
}


