package campus.tech.kakao.map.repository

import android.database.Cursor
import android.util.Log
import campus.tech.kakao.map.db.PlaceDBHelper
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class SavedPlaceRepositoryTest {

    private lateinit var dbHelper: PlaceDBHelper
    private lateinit var savedPlaceRepository: SavedPlaceRepository
    private lateinit var cursor: Cursor

    @Before
    fun setup(){
        dbHelper = mockk(relaxed = true)
        cursor = mockk(relaxed = true)
        savedPlaceRepository = SavedPlaceRepository(dbHelper)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `sqlite에 저장된 장소명들을 잘 불러오는 지 확인`() {
        every { cursor.moveToNext() } returnsMany listOf(true, true, false)
        every { cursor.getString(any()) } returns "저장된 장소명"
        every { dbHelper.readSavedPlaceData() } returns cursor

        val savedPlaceList = savedPlaceRepository.getAllSavedPlace()
        assert(savedPlaceList == listOf<SavedPlace>(SavedPlace("저장된 장소명"), SavedPlace("저장된 장소명")))

        verify { dbHelper.readSavedPlaceData() }

    }

    @Test
    fun `검색어가 이전에 저장되어있지 않은 경우의 데이터 저장`() {
        val place = Place("새로운 장소", "새로운 위치", "카테고리", "x", "y")
        every { cursor.moveToNext() } returnsMany listOf(false)
        every { dbHelper.readSavedPlaceDataWithSamedName("새로운 장소") } returns cursor

        savedPlaceRepository.writePlace(place)

        verify { dbHelper.insertSavedPlaceData(place.name) }
        verify (inverse = true){ dbHelper.deleteSavedPlace(place.name) }
    }

    @Test
    fun `검색어가 이전에 저장되어있는 경우의 데이터 저장`() {
        val place = Place("저장된 장소", "저장된 위치", "카테고리", "x", "y")
        every { cursor.moveToFirst() } returns true
        every { dbHelper.readSavedPlaceDataWithSamedName("저장된 장소") } returns cursor
        every { dbHelper.deleteSavedPlace("저장된 장소") } returns Unit

        savedPlaceRepository.writePlace(place)

        verify { dbHelper.insertSavedPlaceData(place.name) }
        verify { dbHelper.deleteSavedPlace(place.name) }
    }

    @Test
    fun `검색어 삭제가 잘 요청되는지`() {
        val savedPlace = SavedPlace("저장된 장소")
        every { dbHelper.deleteSavedPlace(savedPlace.name) } returns Unit

        savedPlaceRepository.deleteSavedPlace(savedPlace)

        verify { dbHelper.deleteSavedPlace(savedPlace.name) }
    }
}