package campus.tech.kakao.map.data.repository

import android.content.SharedPreferences
import campus.tech.kakao.map.model.Location
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultLocationRepositoryTest {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var locationRepository: DefaultLocationRepository

    @Before
    fun setup() {
        sharedPreferences = mockk()
        editor = mockk()
        locationRepository = DefaultLocationRepository(sharedPreferences)

        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } returns Unit
    }

    @Test
    fun testSaveLocation() {
        // given
        val markerData = Location("Test Place", 123.456, 78.90, "Test Address")

        // when
        locationRepository.saveLocation(markerData)

        // then
        verify { editor.putString(MARKER_PLACE_NAME, markerData.name) }
        verify { editor.putString(MARKER_LATITUDE, markerData.latitude.toString()) }
        verify { editor.putString(MARKER_LONGITUDE, markerData.longitude.toString()) }
        verify { editor.putString(MARKER_ADDRESS, markerData.address) }
        verify { editor.apply() }
    }

    @Test
    fun testLoadLocation() {
        // given
        every { sharedPreferences.getString(MARKER_PLACE_NAME, any()) } returns "Test Place"
        every { sharedPreferences.getString(MARKER_LATITUDE, any()) } returns "123.456"
        every { sharedPreferences.getString(MARKER_LONGITUDE, any()) } returns "78.90"
        every { sharedPreferences.getString(MARKER_ADDRESS, any()) } returns "Test Address"

        // when
        val markerData = locationRepository.loadLocation()

        // then
        assertEquals("Test Place", markerData.name)
        assertEquals(123.456, markerData.latitude, 0.0)
        assertEquals(78.90, markerData.longitude, 0.0)
        assertEquals("Test Address", markerData.address)
    }

    @Test
    fun `loadLocation 함수는 저장된 데이터가 없을 때 기본값을 반환한다`() {
        // given
        every { sharedPreferences.getString(any(), any()) } returns null

        // when
        val markerData = locationRepository.loadLocation()

        // then
        assertEquals("부산대 컴공관", markerData.name)
        assertEquals(35.230934, markerData.latitude, 0.0)
        assertEquals(129.082476, markerData.longitude, 0.0)
        assertEquals("부산광역시 금정구 부산대학로 63번길 2", markerData.address)
    }

    companion object {
        private const val MARKER_PLACE_NAME = "placeName"
        private const val MARKER_LATITUDE = "latitude"
        private const val MARKER_LONGITUDE = "longitude"
        private const val MARKER_ADDRESS = "address"
    }
}
