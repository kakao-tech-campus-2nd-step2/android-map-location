package campus.tech.kakao.map

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.map.model.HistoryDbHelper
import campus.tech.kakao.map.model.LocalSearchService
import campus.tech.kakao.map.model.SearchLocationRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Test
import org.junit.Assert.*

class SearchLocationRepositoryTest {
    // HistoryDbHelper Mock 객체
    private val mockHistoryDbHelper = mockk<HistoryDbHelper>()
    private val mockSQLiteDB = mockk<SQLiteDatabase>()
    private val mockCursor = mockk<Cursor>()

    // LocalSearchService Mock 객체
    private val mockLocalSearchService = mockk<LocalSearchService>()

    // SearchLocationRepository 객체
    private val repository = SearchLocationRepository(mockHistoryDbHelper, mockLocalSearchService)

    @Test
    fun testGetHistory() {
        // given
        every { mockHistoryDbHelper.readableDatabase } returns mockSQLiteDB
        every { mockSQLiteDB.rawQuery(any(), any()) } returns mockCursor
        every { mockCursor.moveToNext() } returnsMany listOf(true, true, true, false)
        every { mockCursor.getString(any()) } returnsMany listOf("test1", "test2", "test3")
        every { mockCursor.getColumnIndexOrThrow(any()) } returns 0
        every { mockCursor.close() } just Runs
        every { mockSQLiteDB.close() } just Runs

        // when
        val result = repository.getHistory()

        // then
        assertArrayEquals(result.toTypedArray(), arrayOf("test1", "test2", "test3"))
    }
}