package campus.tech.kakao.map.ui.search

import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.data.repository.DefaultSavedSearchWordRepository
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import campus.tech.kakao.map.model.SavedSearchWord
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SavedSearchWordViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: SavedSearchWordViewModel
    private lateinit var repository: SavedSearchWordRepository
    private lateinit var dbHelper: SavedSearchWordDBHelper
    private lateinit var searchWord1: SavedSearchWord
    private lateinit var searchWord2: SavedSearchWord

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dbHelper = mockk(relaxed = true)
        repository = DefaultSavedSearchWordRepository(dbHelper)
        viewModel = SavedSearchWordViewModel(repository)

        searchWord1 =
            SavedSearchWord(
                id = 1L,
                name = "부산대병원",
                placeId = "1234",
                address = "부산광역시 서구 구덕로 179",
                latitude = 123.456,
                longitude = 12.34,
            )
        searchWord2 =
            SavedSearchWord(
                id = 2L,
                name = "부산대학교",
                placeId = "1235",
                address = "부산광역시 금정구 부산대학로63번길 2",
                latitude = 124.567,
                longitude = 23.45,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInsertOrUpdateSearchWord() =
        runTest(testDispatcher) {
            // Given
            coEvery { repository.insertOrUpdateSearchWord(any()) } returns Unit
            coEvery { dbHelper.insertOrUpdateSearchWord(any()) } returns Unit
            coEvery { repository.getAllSearchWords() } returns listOf(searchWord1, searchWord2)
            coEvery { dbHelper.getAllSearchWords() } returns listOf(searchWord1, searchWord2)

            // When
            viewModel.insertSearchWord(searchWord1)
            viewModel.updateSavedSearchWords()

            viewModel.insertSearchWord(searchWord2)
            viewModel.updateSavedSearchWords()

            // Then
            assertEquals(listOf(searchWord1, searchWord2), viewModel.savedSearchWords.value)
            coVerify { repository.insertOrUpdateSearchWord(searchWord1) }
            coVerify { repository.insertOrUpdateSearchWord(searchWord2) }

            // init 포함 3번 호출 돼야 함
            coVerify(exactly = 3) { repository.getAllSearchWords() }
        }

    @Test
    fun testDeleteSearchWordById() =
        runTest(testDispatcher) {
            // Given
            coEvery { repository.deleteSearchWordById(searchWord2.id) } returns Unit
            coEvery { dbHelper.deleteSearchWordById(searchWord2.id) } returns Unit
            coEvery { repository.getAllSearchWords() } returns mutableListOf(searchWord1, searchWord2)

            println(viewModel.savedSearchWords.value)
            // When
            viewModel.insertSearchWord(searchWord1)
            viewModel.updateSavedSearchWords()

            viewModel.insertSearchWord(searchWord2)
            viewModel.updateSavedSearchWords()

            viewModel.deleteSearchWordById(searchWord2)

            // Then
            coVerify { repository.deleteSearchWordById(searchWord2.id) }
            assertEquals(1, viewModel.savedSearchWords.value.size)
        }
}
