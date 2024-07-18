package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.model.SavedSearchWord
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class SavedSearchWordRepositoryImplTest {
    private lateinit var dbHelper: SavedSearchWordDBHelper
    private lateinit var repository: SavedSearchWordRepositoryImpl

    @Before
    fun setup() {
        dbHelper = mockk()
        repository = SavedSearchWordRepositoryImpl(dbHelper)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun testInsertOrUpdateSearchWord() {
        // Given
        val searchWord =
            SavedSearchWord(
                name = "부산대병원",
                placeId = "1234",
                address = "부산광역시 서구 구덕로 179",
                latitude = "123.456",
                longitude = "12.34",
            )

        // When
        every { dbHelper.insertOrUpdateSearchWord(searchWord) } just Runs
        repository.insertOrUpdateSearchWord(searchWord)

        // Then
        verify { dbHelper.insertOrUpdateSearchWord(searchWord) }
    }

    @Test
    fun testGetAllSearchWords() {
        // Given
        val expectedList =
            listOf(
                SavedSearchWord(
                    name = "부산대병원",
                    placeId = "1234",
                    address = "부산광역시 서구 구덕로 179",
                    latitude = "123.456",
                    longitude = "12.34",
                ),
                SavedSearchWord(
                    name = "부산대학교",
                    placeId = "1235",
                    address = "부산광역시 금정구 부산대학로63번길 2",
                    latitude = "124.567",
                    longitude = "23.45",
                ),
            )

        // When
        every { dbHelper.getAllSearchWords() } returns expectedList
        val result = repository.getAllSearchWords()

        // Then
        verify { dbHelper.getAllSearchWords() }
        assert(result.size == 2)
        assert(result.containsAll(expectedList))
    }

    @Test
    fun testDeleteSearchWordById() {
        // Given
        val idToDelete = 1L

        // When
        every { dbHelper.deleteSearchWordById(idToDelete) } just Runs
        repository.deleteSearchWordById(idToDelete)

        // Then
        verify { dbHelper.deleteSearchWordById(idToDelete) }
    }
}
