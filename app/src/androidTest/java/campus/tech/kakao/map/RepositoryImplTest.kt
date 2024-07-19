package campus.tech.kakao.map

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import campus.tech.kakao.map.data.PlaceRepositoryImpl
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.util.PlaceContract
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryImplTest {

    private lateinit var repository: PlaceRepositoryImpl
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase(PlaceContract.DATABASE_NAME)
        repository = PlaceRepositoryImpl.getInstance(context)
    }

    @After
    fun after() {
        repository.close()
        context.deleteDatabase(PlaceContract.DATABASE_NAME)
    }

    @Test
    fun testInsertAndGetPlaces() {
        val place1 = Place("1", "Place1", "Address1", "Category1", "10.0", "20.0")
        val place2 = Place("2", "Place2", "Address2", "Category2", "30.0", "40.0")
        val places = listOf(place1, place2)

        repository.updatePlaces(places)

        val result = repository.getAllPlaces()
        assertEquals(2, result.size)
        assertEquals("Place1", result[0].place)
        assertEquals("Place2", result[1].place)
    }

    @Test
    fun testSearchPlaces() {
        val place1 = Place("1", "Gangnam", "Address1", "Category1", "10.0", "20.0")
        val place2 = Place("2", "Gangbuk", "Address2", "Category2", "30.0", "40.0")
        val places = listOf(place1, place2)

        repository.updatePlaces(places)

        val result = repository.getPlaces("Gang")
        assertEquals(2, result.size)
        assertEquals("Gangnam", result[0].place)
        assertEquals("Gangbuk", result[1].place)
    }

    @Test
    fun testLogs() {
        val log1 = Place("1", "Log1", "", "", "", "")
        val log2 = Place("2", "Log2", "", "", "", "")
        val logs = listOf(log1, log2)

        repository.updateLogs(logs)

        var result = repository.getLogs()
        assertEquals(2, result.size)
        assertEquals("Log1", result[0].place)
        assertEquals("Log2", result[1].place)

        repository.removeLog("1")
        result = repository.getLogs()
        assertEquals(1, result.size)
        assertEquals("Log2", result[0].place)
    }
}