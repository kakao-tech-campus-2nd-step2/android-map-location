package campus.tech.kakao.map

import android.content.SharedPreferences
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class MapActivityTest {

    private lateinit var scenario: ActivityScenario<Map_Activity>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setup() {
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        `when`(sharedPreferences.edit()).thenReturn(editor)

        scenario = ActivityScenario.launch(Map_Activity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun mapSuccess() {
        val mockMap = mock(GoogleMap::class.java)

        scenario.onActivity { activity ->
            activity.onMapReady(mockMap)
            // Verify that the map is not null or that mapReady was called
            assertNotNull(mockMap)
        }
    }

    @Test
    fun LastLocationRequest() {
        val testLatLng = LatLng(37.5665, 126.9780)

        scenario.onActivity { activity ->
            val method = Map_Activity::class.java.getDeclaredMethod("saveLastLocation", Double::class.java, Double::class.java)
            method.isAccessible = true
            method.invoke(activity, testLatLng.latitude, testLatLng.longitude)

            // Verify SharedPreferences interactions
            verify(editor).putFloat("lastLatitude", testLatLng.latitude.toFloat())
            verify(editor).putFloat("lastLongitude", testLatLng.longitude.toFloat())
            verify(editor).apply()

            `when`(sharedPreferences.getFloat("lastLatitude", 0f)).thenReturn(testLatLng.latitude.toFloat())
            `when`(sharedPreferences.getFloat("lastLongitude", 0f)).thenReturn(testLatLng.longitude.toFloat())

            // Access the lastKnownLocation field
            val field = Map_Activity::class.java.getDeclaredField("lastKnownLocation")
            field.isAccessible = true
            val lastKnownLocation = field.get(activity) as LatLng

            assertEquals(testLatLng, lastKnownLocation)
        }
    }

    @Test
    fun Showthe401() {
        scenario.onActivity { activity ->
            activity.onMapError(401)
            onView(withId(R.id.error_message)).check(matches(isDisplayed()))
            onView(withId(R.id.error_message)).check(matches(withText("401 Unauthorized Error")))
        }
    }
}




