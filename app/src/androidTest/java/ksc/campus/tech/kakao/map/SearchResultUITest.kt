package ksc.campus.tech.kakao.map

import android.app.Application
import android.content.Context.RECEIVER_EXPORTED
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import ksc.campus.tech.kakao.map.view_models.SearchActivityViewModel
import ksc.campus.tech.kakao.map.views.MainActivity
import ksc.campus.tech.kakao.map.views.fragments.KakaoMapFragment
import ksc.campus.tech.kakao.map.views.fragments.SearchActivityFragmentFactory
import ksc.campus.tech.kakao.map.views.fragments.SearchResultFragment
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Objects

class SearchResultUITest {
    /**
     * UI 테스트를 위한 더미 레포지토리 클래스로 FakeSearchResultRepository 사용
     *
     * 검색 결과값은 검색어에 따라 결정
     * X를 검색 시, 검색 결과의 이름은 각 각 ["name X 0", "name X 1", ... , "name X 15"]
     * X를 검색 시, 검색 결과의 주소는 각 각 ["address X 0", "address X 1", ... , "address X 15"]
     */

    lateinit var scenario: FragmentScenario<SearchResultFragment>
    private fun checkTextExists(text:String){
        Espresso.onView(ViewMatchers.withId(R.id.list_search_result))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.hasDescendant(
                        ViewMatchers.withText(text)
                    )
                )
            )
    }

    @Before
    fun prepareFragment(){
        val viewModel = SearchActivityViewModel(ApplicationProvider.getApplicationContext())
        val factory = SearchActivityFragmentFactory(viewModel)
        scenario = launchFragmentInContainer<SearchResultFragment>(null, factory = factory)
    }

    @Test
    fun searchResultAppearOnListViewOnSearch(){

        // given
        val query = "Hello"

        // when
        scenario.withFragment {
            this.viewModel.submitQuery(query)
        }

        // then
        for(i in 0..5) {
            checkTextExists("name Hello $i")
            checkTextExists("address Hello $i")
        }
    }

}