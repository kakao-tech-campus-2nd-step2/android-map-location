package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val mainActivity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun 검색창에_검색어를_입력한다(){
        onView(withId(R.id.etSearch))
            .perform(replaceText("입력테스트"))
            .check(matches(withText("입력테스트")))
    }
    @Test
    fun 검색창의_x버튼을_누르면_검색어가_삭제된다() {
        onView(withId(R.id.etSearch))
            .perform(replaceText("삭제테스트"))

        onView(withId(R.id.btnClose))
            .perform(click())

        onView(withId(R.id.etSearch))
            .check(matches(withText("")))
    }
    @Test
    fun 장소를_선택하면_지도가_펼쳐진다() {
        Intents.init()
        onView(withId(R.id.etSearch)).perform(replaceText("성심당본점"))
        Thread.sleep(1000)
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )
        Intents.intended(IntentMatchers.hasComponent(MapActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun 검색어를_입력하면_목록이_나타난다() {
        onView(withId(R.id.etSearch))
            .perform(replaceText("성"))

        Thread.sleep(1000)

        onView(withId(R.id.tvNoResult))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }


//    @Test
//    fun 지도화면에서_뒤로가기를_두번누르면_검색화면이_펼쳐진다() {
//        Intents.init()
//        onView(withId(R.id.etSearch)).perform(replaceText("성심당본점"))
//        Thread.sleep(1000)
//        onView(withId(R.id.recyclerView))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                    0, click()
//                )
//            )
//        Intents.intended(IntentMatchers.hasComponent(MapActivity::class.java.name))
//
//        Thread.sleep(800)
//        pressBack()
//        pressBack()
//
//        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
//        Intents.release()
//    }
}