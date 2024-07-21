package campus.tech.kakao.map

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.activity.DataSearchActivity
import campus.tech.kakao.map.activity.HomeMapActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeMapActivityTest {
    @get:Rule
    val activityRule: ActivityScenarioRule<HomeMapActivity> =
        ActivityScenarioRule(
            Intent(
                ApplicationProvider.getApplicationContext(),
                HomeMapActivity::class.java
            ).apply {
                putExtra("name", "전남대학교 여수캠퍼스")
                putExtra("address", "전라남도 여수시 대학로 50")
                putExtra("latitude", "34.772884563341")
                putExtra("longitude", "127.69954169563")
            }
        )

    @Before
    fun 인텐트_초기화() {
        init()
    }

    @Test
    fun 목록에서_선택한_장소의_정보가_BottomSheet_에_나타남() {
        onView(withId(R.id.placeName))
            .check(matches(withText("전남대학교 여수캠퍼스")))
        onView(withId(R.id.placeAddress))
            .check(matches(withText("전라남도 여수시 대학로 50")))
        onView(withId(R.id.bottomSheet))
            .check(matches(isDisplayed()))
    }

    @Test
    fun 검색바를_클릭하면_검색화면으로_이동() {
        onView(withId(R.id.search_home))
            .perform(click())
        intended(hasComponent(DataSearchActivity::class.java.name))
        release()
    }
}