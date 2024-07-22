package campus.tech.kakao.map.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.model.SavedSearchWord
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SavedSearchWordDBHelperTest {
    private lateinit var dbHelper: SavedSearchWordDBHelper
    private lateinit var searchWord1: SavedSearchWord
    private lateinit var searchWord2: SavedSearchWord

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = SavedSearchWordDBHelper(context)
        searchWord1 =
            SavedSearchWord(
                name = "부산대병원",
                placeId = "1234",
                address = "부산광역시 서구 구덕로 179",
                latitude = 123.456,
                longitude = 12.34,
            )
        searchWord2 =
            SavedSearchWord(
                name = "부산대학교",
                placeId = "1235",
                address = "부산광역시 금정구 부산대학로63번길 2",
                latitude = 124.567,
                longitude = 23.45,
            )
    }

    @After
    fun tearDown() {
        dbHelper.writableDatabase.use { db ->
            db.execSQL("DELETE FROM ${SavedSearchWordDBHelper.TABLE_NAME}")
        }
    }

    @Test
    fun testInsertOrUpdateSearchWord() {
        // when : 검색어 2개 insert
        dbHelper.insertOrUpdateSearchWord(searchWord1)
        dbHelper.insertOrUpdateSearchWord(searchWord2)

        // then : 검색어 2개가 저장 됐는지 확인
        val savedSearchWords = dbHelper.getAllSearchWords()
        assertEquals(2, savedSearchWords.size)

        // when : 같은 검색어가 들어갈 때 update 되는지 확인
        dbHelper.insertOrUpdateSearchWord(searchWord1)

        // then
        val updatedWords = dbHelper.getAllSearchWords()
        assertEquals(2, updatedWords.size)
        val updatedWord = updatedWords.find { it.placeId == "1234" }
        assertEquals("부산대병원", updatedWord?.name)
        assertEquals("부산광역시 서구 구덕로 179", updatedWord?.address)
    }

    @Test
    fun testDeleteSearchWord() {
        // when
        dbHelper.insertOrUpdateSearchWord(searchWord1)
        dbHelper.insertOrUpdateSearchWord(searchWord2)

        // then
        val initialWords = dbHelper.getAllSearchWords()
        assertEquals(2, initialWords.size)

        // when : 검색어 하나를 삭제 했을 때(auto Increment 고려)
        dbHelper.deleteSearchWordById(4) // insert 테스트랑 같이 실행할 경우 시작 id가 4로 들어감
        dbHelper.deleteSearchWordById(1) // 이 테스트만 할 경우 시작 id가 1로 들어감

        // then : 2번째 검색어 하나가 남는 지 확인
        val remainingWords = dbHelper.getAllSearchWords()
        assertEquals(1, remainingWords.size)
        assertEquals("부산대학교", remainingWords[0].name)
    }
}
