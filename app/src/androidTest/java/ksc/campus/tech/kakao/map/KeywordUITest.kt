package ksc.campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import kotlinx.coroutines.runBlocking
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository
import ksc.campus.tech.kakao.map.views.MainActivity
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test


class KeywordUITest {
    /**
     * UI 테스트를 위한 더미 레포지토리 클래스로 FakeKeywordRepository 사용
     *
     * keywords 기본값으로 "1", "2", "hello", "world" 포함
     * addKeyword(), deleteKeyword() 호출 시 별도의 db 작업 없이 즉시 데이터에 반영
     */


    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun keywordAddOnMethodCalled(){

        // given
        val checkingKeyword = "AAFFCC"
        checkNoTextExists(checkingKeyword)

        // when
        activityScenarioRule.scenario.onActivity {
            val repository = (it.application as MyApplication).appContainer.getSingleton<SearchKeywordRepository>()

            runBlocking {
                repository.addKeyword(checkingKeyword)
            }
        }

        // then
        checkTextExists(checkingKeyword)
    }

    @Test
    fun keywordRemovedOnMethodCalled() {

        // given
        val checkingKeyword = "hello"
        checkTextExists(checkingKeyword)

        // when
        activityScenarioRule.scenario.onActivity {
            val repository =
                (it.application as MyApplication).appContainer.getSingleton<SearchKeywordRepository>()

            runBlocking {
                repository.deleteKeyword(checkingKeyword)
            }
        }

        // wait until animation of ListAdapter finished
        Thread.sleep(100)

        // then
        checkNoTextExists(checkingKeyword)
    }

    private fun checkNoTextExists(text:String){
        onView(allOf(isDescendantOfA(withId(R.id.saved_search_bar)), withText(text), isDisplayed()))
            .check((doesNotExist()))
    }

    private fun checkTextExists(text:String){
        onView(withId(R.id.saved_search_bar))
            .check(
                matches(
                    hasDescendant(
                        withText(text)
                    )
                )
            )
    }
}