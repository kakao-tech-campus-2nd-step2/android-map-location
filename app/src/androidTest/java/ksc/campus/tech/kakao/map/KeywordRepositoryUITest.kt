package ksc.campus.tech.kakao.map

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import kotlinx.coroutines.runBlocking
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository
import ksc.campus.tech.kakao.map.views.MainActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test




class KeywordRepositoryUITest {
    /**
     * UI 테스트를 위한 더미 레포지토리 클래스로 FakeKeywordRepository 사용
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
    fun keywordRemovedOnMethodCalled(){

        // given
        val checkingKeyword = "hello"
        checkTextExists(checkingKeyword)

        // when
        activityScenarioRule.scenario.onActivity {
            val repository = (it.application as MyApplication).appContainer.getSingleton<SearchKeywordRepository>()

            runBlocking {
                repository.deleteKeyword(checkingKeyword)
            }
        }

        // then
        checkNoTextExists(checkingKeyword)
    }

    private fun checkNoTextExists(text:String){
        onView(allOf(withParent(withId(R.id.saved_search_bar)), withText(text)))
            .check(doesNotExist())
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

    companion object{
        fun hasItem(matcher: Matcher<View?>): BoundedMatcher<View?, RecyclerView> {
            return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: org.hamcrest.Description) {
                    description.appendText("has item: ")
                    matcher.describeTo(description)
                }

                override fun matchesSafely(view: RecyclerView): Boolean {
                    val adapter = view.adapter
                    for (position in 0 until adapter!!.itemCount) {
                        val type = adapter.getItemViewType(position)
                        val holder = adapter.createViewHolder(view, type)
                        adapter.onBindViewHolder(holder, position)
                        if (matcher.matches(holder.itemView)) {
                            return true
                        }
                    }
                    return false

                }
            }
        }
    }

}