package campus.tech.kakao.map

import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.model.LocationDbHelper
import campus.tech.kakao.map.model.SavedLocation
import campus.tech.kakao.map.model.datasource.LocationLocalDataSource
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(AndroidJUnit4::class)
class LocationLocalDataSourceTest {
    private lateinit var locationLocalDataSource: LocationLocalDataSource
    private lateinit var locationDbHelper: LocationDbHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private lateinit var savedLocations: List<SavedLocation>

    @Before
    fun init() {
        locationDbHelper = LocationDbHelper(ApplicationProvider.getApplicationContext())
        locationLocalDataSource = LocationLocalDataSource(locationDbHelper)

        savedLocations = listOf(
            SavedLocation("Location 1"),
            SavedLocation("Location 2"),
            SavedLocation("Location 3")
        )

        sqLiteDatabase = locationDbHelper.writableDatabase
        savedLocations.forEach {
            locationLocalDataSource.addSavedLocation(it.title)
        }
    }

    @Test
    fun `사용자가_선택한_위치가_데이터베이스에_저장된다`() {
        val newLocationTitle = "New Location"
        val result: Long = locationLocalDataSource.addSavedLocation(newLocationTitle)
        assertEquals(4L, result)
    }

    @Test
    fun `데이터베이스에_저장된_위치를_모두_불러온다`() {
        val result = locationLocalDataSource.getSavedLocationAll()
        assertEquals(savedLocations.size, result.size)
        for (i in savedLocations.indices) {
            assertEquals(savedLocations[i].title, result[i].title)
        }
    }

    @Test
    fun `저장된_위치를_삭제한다`() {
        val titleToDelete = savedLocations[0].title
        val result = locationLocalDataSource.deleteSavedLocation(titleToDelete)
        assertEquals(1, result)

        // 삭제 후 확인
        val results = locationLocalDataSource.getSavedLocationAll()
        assertEquals(savedLocations.size - 1, results.size)
    }

    @After
    fun tearDown() {
        // 데이터베이스 정리
        locationDbHelper.close()
    }
}