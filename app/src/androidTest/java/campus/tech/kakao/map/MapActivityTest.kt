package campus.tech.kakao.map

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.view.MapActivity
import com.google.gson.Gson
import com.kakao.vectormap.MapView
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MapActivityTest {
    private lateinit var selectedLocation: LocationData

    @Before
    fun setUp() {
        selectedLocation = LocationData("Test Location", "시청", "시청", 37.566, 126.978)
    }

    @Test
    fun testMapViewDisplayed() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MapActivity::class.java).apply {
            putExtra("selectedLocation", Gson().toJson(selectedLocation))
        }
        ActivityScenario.launch<MapActivity>(intent).use {
            onView(ViewMatchers.withId(R.id.map_view))
        }
    }

    @Test
    fun testInputTextDisplayed() {
        ActivityScenario.launch(MapActivity::class.java).use {
            onView(ViewMatchers.withId(R.id.MapinputText))
        }
    }

    @Test
    fun testRecyclerViewDisplayed() {
        ActivityScenario.launch(MapActivity::class.java).use {
            onView(ViewMatchers.withId(R.id.recyclerView))
        }
    }

    @Test
    fun testMapViewInitializedWithSelectedLocation() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MapActivity::class.java).apply {
            putExtra("selectedLocation", Gson().toJson(selectedLocation))
        }
        ActivityScenario.launch<MapActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                val mapView = activity.findViewById<MapView>(R.id.map_view)
                assertNotNull(mapView)
            }
        }
    }
}