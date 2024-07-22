package campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MapUnitTest {
    lateinit var sharedPreferences: SharedPreferences

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    @After
    fun after() {
        sharedPreferences.edit().clear().apply()
    }

    @Test
    fun exitAppAndSaveLocation() {
        val scenario = ActivityScenario.launch(MapActivity::class.java)

        scenario.onActivity { activity ->
            val latitude = 35.0
            val longitude = 129.0
            activity.saveData(latitude.toString(), longitude.toString())

            activity.loadData()

            val savedLatitude = sharedPreferences.getString("latitude", null)?.toDoubleOrNull()
            val savedLongitude = sharedPreferences.getString("longitude", null)?.toDoubleOrNull()

            assertEquals(latitude, savedLatitude)
            assertEquals(longitude, savedLongitude)
        }

        scenario.close()
    }
}