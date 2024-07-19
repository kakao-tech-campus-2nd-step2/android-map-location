package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.view.map.MapActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapSdk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class MapActivityUiTest {
    @get: Rule
    var activityRule = ActivityScenarioRule(MapActivity::class.java)

    @Test
    fun `지도에서_에러_발생_시_에러_메세지가_나타난다`() { // 테스트를 통과하지 않습니다.. 지도 에러를 발생시키는 부분에서 오류가 나는 것 같아요ㅠㅠ 어렵습니다...
        activityRule.scenario.onActivity { activity ->
            KakaoMapSdk.init(activity, "fakeKey");
        }
        onView(withId(R.id.errorMessageTextView)).check(matches(isDisplayed()))
    }
}