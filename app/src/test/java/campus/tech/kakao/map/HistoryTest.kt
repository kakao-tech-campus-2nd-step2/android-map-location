package campus.tech.kakao.map

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.data.repository.HistoryRepositoryImpl
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.domain.repository.HistoryRepository
import campus.tech.kakao.map.presentation.MapActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HistoryTest {
    lateinit var context: Context
    lateinit var dbHelper: MapDbHelper
    lateinit var historyRepositoryImpl: HistoryRepositoryImpl

    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
        dbHelper = MapDbHelper(context)
        historyRepositoryImpl = HistoryRepositoryImpl(dbHelper)
    }

    @Test
    fun `검색 기록 저장 테스트`() {
        val history = mutableListOf<Location>()
        for (i in 1..10) {
            val testLoc = Location(
                "$i",
                "testName$i",
                "TEST",
                "Test시 Test구 Test로 $i",
                i + 11.0,i + 100.0)
            history.add(testLoc)
            historyRepositoryImpl.insertHistory(testLoc)
        }
        val savedHistory = historyRepositoryImpl.getAllHistory()
        history.forEachIndexed { index, location ->
            assertEquals(savedHistory[index], location)
        }
    }

    @Test
    fun `ID가 일치하면 같은 Location으로 처리하여 중복으로 저장하지 않음`() {
        for (i in 1..10) {
            val testLoc = Location(
                "0",
                "$i",
                "$i",
                "${i}시 ${i}구 ${i}로",
                1.0 + i,1.0 + i)
            historyRepositoryImpl.insertHistory(testLoc)
        }
        val savedHistory = historyRepositoryImpl.getAllHistory()
        assertEquals(savedHistory.size, 1)
    }
}