package campus.tech.kakao.map

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.map.model.HistoryDbHelper
import campus.tech.kakao.map.model.LocalSearchDocument
import campus.tech.kakao.map.model.LocalSearchMeta
import campus.tech.kakao.map.model.LocalSearchResponse
import campus.tech.kakao.map.model.LocalSearchSameName
import campus.tech.kakao.map.model.LocalSearchService
import campus.tech.kakao.map.model.SearchLocationRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import retrofit2.Response

class SearchLocationRepositoryTest {
    // HistoryDbHelper Mock 객체
    private val mockHistoryDbHelper = mockk<HistoryDbHelper>()
    private val mockSQLiteDB = mockk<SQLiteDatabase>()
    private val mockCursor = mockk<Cursor>()

    // LocalSearchService Mock 객체
    private val mockLocalSearchService = mockk<LocalSearchService>()

    // SearchLocationRepository 객체
    private val repository = SearchLocationRepository(mockHistoryDbHelper, mockLocalSearchService)

    @Before
    fun setUp() {
        every { mockHistoryDbHelper.readableDatabase } returns mockSQLiteDB
        every { mockHistoryDbHelper.writableDatabase } returns mockSQLiteDB
        every { mockSQLiteDB.rawQuery(any(), any()) } returns mockCursor
        every { mockSQLiteDB.close() } just Runs
        every { mockCursor.close() } just Runs
    }

    @Test
    fun testGetHistory() {
        // given
        every { mockCursor.moveToNext() } returnsMany listOf(true, true, true, false)
        every { mockCursor.getString(any()) } returnsMany listOf("test1", "test2", "test3")
        every { mockCursor.getColumnIndexOrThrow(any()) } returns 0
        // when
        val result = repository.getHistory()

        // then
        assertArrayEquals(result.toTypedArray(), arrayOf("test1", "test2", "test3"))
    }

    @Test
    fun testAddHistory_ItemExist() {
        // given
        every { mockCursor.count } returns 1
        every { mockSQLiteDB.execSQL(any()) } just Runs
        every { mockSQLiteDB.insert(any(), any(), any()) } returns 1
        mockkConstructor(ContentValues::class)
        every { anyConstructed<ContentValues>().put(any(), any<String>()) } just Runs

        // when
        repository.addHistory("testCategory")

        // then
        verify { mockSQLiteDB.execSQL(any()) }
        verify { mockSQLiteDB.insert(any(), any(), any()) }
    }

    @Test
    fun testAddHistory_ItemNotExist() {
        // given
        every { mockCursor.count } returns 0
        every { mockSQLiteDB.execSQL(any()) } just Runs
        every { mockSQLiteDB.insert(any(), any(), any()) } returns 1
        mockkConstructor(ContentValues::class)
        every { anyConstructed<ContentValues>().put(any(), any<String>()) } just Runs

        // when
        repository.addHistory("testCategory")

        // then
        verify(exactly = 0) { mockSQLiteDB.execSQL(any()) }
        verify { mockSQLiteDB.insert(any(), any(), any()) }
    }

    @Test
    fun testRemoveHistory() {
        // given
        every { mockSQLiteDB.execSQL(any()) } just Runs

        // when
        repository.removeHistory("testCategory")

        // then
        verify { mockSQLiteDB.execSQL(match { it.startsWith("DELETE") }) }
    }

    @Test
    fun testSearchLocation() = runBlocking {
        // given
        val testDocuments = listOf(
            LocalSearchDocument(
                "address_name1", "category_group_code1", "category_group_name1",
                "category_name1", "distance1", "id1", "phone1", "place_name1",
                "place_url1", "road_address_name1", "1.0", "1.0"
            ),
            LocalSearchDocument(
                "address_name2", "category_group_code2", "category_group_name2",
                "category_name2", "distance2", "id2", "phone2", "place_name2",
                "place_url2", "road_address_name2", "2.0", "2.0"
            )
        )
        val testMeta = LocalSearchMeta(
            true, 1, LocalSearchSameName(
                "keyword", listOf("region"), "selected_region"
            ), 2
        )
        val mockResponse = Response.success(LocalSearchResponse(testDocuments, testMeta))
        coEvery { mockLocalSearchService.requestLocalSearch(query = any()) } returns mockResponse

        // when
        val result = repository.searchLocation("testCategory")

        // then
        assertEquals(result.size, 2)
        assertEquals(result[0].name, "place_name1")
        assertEquals(result[0].address, "address_name1")
        assertEquals(result[0].category, "category_group_name1")
        assertEquals(result[0].latitude, 1.0, 0.0)
        assertEquals(result[0].longitude, 1.0, 0.0)
        assertEquals(result[1].name, "place_name2")
        assertEquals(result[1].address, "address_name2")
        assertEquals(result[1].category, "category_group_name2")
        assertEquals(result[1].latitude, 2.0, 0.0)
        assertEquals(result[1].longitude, 2.0, 0.0)
    }
}