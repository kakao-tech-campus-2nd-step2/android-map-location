package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewUiTest {

    @get:Rule
    val xml = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun `상단X버튼_클릭시_입력창이_비워지는가`(){
        val inputEditView = onView(withId(R.id.input))
        val xButton = onView(withId(R.id.close_button))

        inputEditView.perform(typeText("Something"))
        xButton.perform(click())

        inputEditView.check(matches(withText("")))
    }

    @Test
    fun `임의의_문자_입력시_리사이클러뷰가_보이는가`(){
        val inputEditView = onView(withId(R.id.input))
        val noResultTextView = onView(withId(R.id.no_result_textview))
        val resultRecyclerView = onView(withId(R.id.recyclerView))

        // EditText에 값을 입력한다
        inputEditView.perform(typeText("Something"), closeSoftKeyboard())

        // noResultTextView는 이제 보이지 않고 resultRecyclerVIew(검색결과)가 표시된다
        noResultTextView.check(matches(withEffectiveVisibility(Visibility.GONE)))
        resultRecyclerView.check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}