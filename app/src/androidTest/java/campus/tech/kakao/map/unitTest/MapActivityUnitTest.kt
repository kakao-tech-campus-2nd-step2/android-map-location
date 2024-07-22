package campus.tech.kakao.map.unitTest

import android.content.Context
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.R
import campus.tech.kakao.map.ui.MapActivity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityUnitTest {

    private lateinit var scenario: ActivityScenario<MapActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MapActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    // MapActivity가 잘 보이나요?
    @Test
    fun testMapActivityVisible() {
        scenario.onActivity { activity ->
            val mainView = activity.findViewById<View>(R.id.main)
            assertNotNull(mainView)
        }
    }

    // 저장된 위치를 잘 로드하나요?
    @Test
    fun testSavedLocationLoad() {
        scenario.onActivity { activity ->
            val preferences = activity.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            val savedLatitude = 37.3957122
            val savedLongitude = 127.1105181
            editor.putString("latitude", savedLatitude.toString())
            editor.putString("longitude", savedLongitude.toString())
            editor.apply()

            activity.loadSavedLocation()

            assertEquals(savedLatitude, activity.savedLatitude)
            assertEquals(savedLongitude, activity.savedLongitude)
        }
    }

    // 현재 위치를 잘 저장하나요?
    @Test
    fun testSaveCurrentLocation() {
        scenario.onActivity { activity ->
            val latitude = 37.0
            val longitude = 127.0

            activity.savedLatitude = latitude
            activity.savedLongitude = longitude

            activity.saveCurrentLocation()

            val preferences = activity.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
            val savedLatitude = preferences.getString("latitude", null)?.toDouble()
            val savedLongitude = preferences.getString("longitude", null)?.toDouble()

            assertEquals(latitude, savedLatitude)
            assertEquals(longitude, savedLongitude)
        }
    }

}