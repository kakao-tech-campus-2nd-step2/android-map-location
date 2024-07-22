package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import campus.tech.kakao.map.presentation.MapActivity
import campus.tech.kakao.map.presentation.PlaceActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlaceActivityUITest {

    @get:Rule
    val activityRule = ActivityTestRule(PlaceActivity::class.java)

    @Test
    fun 장소_키워드_검색시_목록_조회_확인() {

        // 1. 검색어 입력창(etSearch)에 검색어 입력
        var searchQuery = "d"
        onView(withId(R.id.etSearch))
            .perform(ViewActions.typeText(searchQuery))

        // 2. 장소 목록(rvPlaceList)에 검색 결과가 표시되는지 확인
        Thread.sleep(1000)
        onView(withId(R.id.rvPlaceList))
            .check(ViewAssertions.matches(hasMinimumChildCount(1)))
    }

    // 보니까 키패드가 영어로 되어있으면 영어만 쳐져서 저게 안되는 듯?
    @Test
    fun 목록_선택시_화면_전환_여부_확인() {

        Intents.init()

        try {
            // 1. 검색어 입력창(etSearch)에 검색어 입력
            val searchQuery = "d"
            onView(withId(R.id.etSearch))
                .perform(ViewActions.typeText(searchQuery))

            // 2. 일정 시간 대기
            Thread.sleep(1000)

            // 3. 장소 목록(rvPlaceList)의 첫 번째 항목 클릭
            onView(withId(R.id.rvPlaceList))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0, ViewActions.click()
                    )
                )

            // 4. 다음 화면(MapActivity)으로의 인텐트가 시작되었는지 확인
            Intents.intended(IntentMatchers.hasComponent(MapActivity::class.java.name))


        } finally {
            Intents.release()
        }
    }

}