package campus.tech.kakao.map

import campus.tech.kakao.map.model.search.Place
import campus.tech.kakao.map.model.search.SearchKeyword
import campus.tech.kakao.map.repository.search.SavedSearchKeywordRepository
import campus.tech.kakao.map.repository.search.SearchRepository
import campus.tech.kakao.map.viewmodel.search.SearchViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val TEST_SEARCH_KEYWORD = "TEST_SEARCH_KEYWORD"
private const val TEST_PLACE_NAME = "TEST_PLACE_NAME"
private const val TEST_PLACE_CATEGORY = "TEST_PLACE_CATEGORY"
private const val TEST_PLACE_ADDRESS = "TEST_PLACE_ADDRESS"
private const val TEST_PLACE_X = "36.37003"
private const val TEST_PLACE_Y = "127.34594"

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val searchRepository = mockk<SearchRepository>(relaxed = true)
    private val savedSearchKeywordRepository = mockk<SavedSearchKeywordRepository>(relaxed = true)
    private val searchViewModel = SearchViewModel(searchRepository, savedSearchKeywordRepository)

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun a() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetSearchResults() {
        // given
        val searchKeyword = SearchKeyword(TEST_SEARCH_KEYWORD)
        val place = Place(
            TEST_PLACE_NAME, TEST_PLACE_CATEGORY, TEST_PLACE_ADDRESS, TEST_PLACE_X,
            TEST_PLACE_Y
        )
        val searchResults = listOf(place)
        coEvery { searchRepository.Search(searchKeyword) } returns searchResults

        // when
        searchViewModel.getSearchResults(searchKeyword)

        // then
        assert(searchViewModel.searchResults.value == searchResults)
        coVerify { searchRepository.Search(searchKeyword) }

    }

    @Test
    fun testSaveSearchKeyword() {
        // given
        val searchKeyword = SearchKeyword(TEST_SEARCH_KEYWORD)
        val savedSearchKeywords = listOf(searchKeyword)
        coEvery { savedSearchKeywordRepository.getSavedSearchKeywords() } returns savedSearchKeywords

        //when
        searchViewModel.saveSearchKeyword(searchKeyword)

        //then
        assert(searchViewModel.savedSearchKeywords.value == savedSearchKeywords)
        coVerify { savedSearchKeywordRepository.saveSearchKeyword(searchKeyword) }
    }

    @Test
    fun testGetSavedSearchKeywords() {
        // given
        val searchKeyword = SearchKeyword(TEST_SEARCH_KEYWORD)
        val savedSearchKeywords = listOf(searchKeyword)
        coEvery { savedSearchKeywordRepository.getSavedSearchKeywords() } returns savedSearchKeywords

        // when
        searchViewModel.getSavedSearchKeywords()

        // then
        assert(searchViewModel.savedSearchKeywords.value == savedSearchKeywords)
        coVerify { savedSearchKeywordRepository.getSavedSearchKeywords() }
    }

    @Test
    fun testDelSavedSearchKeyword() {
        // given
        val searchKeyword = SearchKeyword(TEST_SEARCH_KEYWORD)
        val savedSearchKeywords = emptyList<SearchKeyword>()
        coEvery { savedSearchKeywordRepository.getSavedSearchKeywords() } returns savedSearchKeywords

        //when
        searchViewModel.delSavedSearchKeyword(searchKeyword)

        //then
        assert(searchViewModel.savedSearchKeywords.value == savedSearchKeywords)
        coVerify { savedSearchKeywordRepository.delSavedSearchKeyword(searchKeyword) }
    }
}