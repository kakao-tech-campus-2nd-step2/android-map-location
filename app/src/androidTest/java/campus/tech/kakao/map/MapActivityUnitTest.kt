package campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class MapActivityUnitTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MapActivity::class.java)

    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sharedPreferences = context.getSharedPreferences("lastLatLng", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    @Test
    fun testSaveLatLng() {
        val latitude = "35.231627"
        val longitude = "129.084020"

        activityScenarioRule.scenario.onActivity { activity ->
            activity.saveLatLng(latitude, longitude)

            val savedLat = sharedPreferences.getString("lastLat", null)
            val savedLng = sharedPreferences.getString("lastLng", null)

            assertEquals(latitude, savedLat)
            assertEquals(longitude, savedLng)
        }
    }
}
