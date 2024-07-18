import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.View.MapFragment
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class MapFragmentUnitTest {
        private lateinit var fragmentScenario: FragmentScenario<MapFragment>

        @Before
        fun setUp() {
            fragmentScenario = FragmentScenario.launchInContainer(MapFragment::class.java)
        }

        @After
        fun tearDown() {
            fragmentScenario.close()
        }

        @Test
        fun testGetLastLocation() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("MapPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putFloat("lastY", 37.402005f).putFloat("lastX", 127.108621f).apply()

            fragmentScenario.onFragment { fragment   ->
                val lastLocation = fragment.getLastLocation(context)

                assertEquals(37.402005, lastLocation.first)
                assertEquals(127.108621, lastLocation.second)
            }
        }

        @Test
        fun testSaveLastLocation() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("MapPrefs", Context.MODE_PRIVATE)

            fragmentScenario.onFragment { fragment   ->
                fragment.saveLastLocation(37.402005, 127.108621)

                val lastY = sharedPreferences.getFloat("Y", 0f)
                val lastX = sharedPreferences.getFloat("X", 0f)

                assertEquals(37.402005f, lastY)
                assertEquals(127.108621f, lastX)
            }
        }

        @Test
        fun testSetCoordinates() {
            fragmentScenario.onFragment { fragment ->
                fragment.setCoordinates(127.0, 37.0, "PlaceName", "RoadAddressName")
                 assertEquals(127.0, fragment.x)
                 assertEquals(37.0, fragment.y)
            }
        }


    }