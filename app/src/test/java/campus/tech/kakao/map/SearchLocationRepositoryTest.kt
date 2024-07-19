package campus.tech.kakao.map

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.map.model.HistoryDbHelper
import campus.tech.kakao.map.model.LocalSearchService
import campus.tech.kakao.map.model.SearchLocationRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

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
}