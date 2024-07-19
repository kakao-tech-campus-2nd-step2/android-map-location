package campus.tech.kakao.map

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.slowSwipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.data.PreferenceHelper
import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.presentation.MapActivity
import campus.tech.kakao.map.presentation.PlaceActivity
import com.kakao.vectormap.LatLng
import io.mockk.mockkObject
import io.mockk.verify
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MapActivity::class.java)


    @Test
    fun 검색필드가_표시되는지_확인_테스트() {
        onView(withId(R.id.searchBox)).check(matches(isDisplayed()))
    }

    @Test
    fun 검색필드의_힌트텍스트_확인_테스트() {
        onView(withId(R.id.searchTextView)).check(matches(withHint("검색어를 입력해 주세요")))
    }

    @Test
    fun 맵뷰가_표시되는지_확인_테스트() {
        onView(withId(R.id.mapView)).check(matches(isDisplayed()))
    }

    @Test
    fun 검색필드를_누르면_액티비티_전환되는지_확인_테스트() {
        Intents.init()
        onView(withId(R.id.searchBox)).perform(click())
        Intents.intended(hasComponent(PlaceActivity::class.java.name))
        Intents.release()
    }


    @Test
    fun 인텐트가_있을때_바텀시트가_표시되는지_확인() {
        val place = PlaceVO("가짜 장소", "가짜 주소", "카페", 37.5665, 126.9780)
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), MapActivity::class.java).apply {
                putExtra("place", place)
            }

        launchActivity<MapActivity>(intent)
        onView(withId(R.id.bottomSheet)).check(matches(isDisplayed()))
    }

    @Test
    fun 바텀시트에_표시된_텍스트가_인텐트로_보낸것과_일치하는지_확인() {
        val place = PlaceVO("가짜 장소", "가짜 주소", "카페", 37.5665, 126.9780)
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), MapActivity::class.java).apply {
                putExtra("place", place)
            }
        launchActivity<MapActivity>(intent)

        onView(withId(R.id.nameTextaView)).check(matches(withText("가짜 장소")))
        onView(withId(R.id.addressTextView)).check(matches(withText("가짜 주소")))
    }

    @Test
    fun 인텐트와_마지막장소가_없다면_디폴트맵_표시하는지_확인_테스트() {

        // 마지막 장소 초기화
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sharedPreferences = context.getSharedPreferences("kakao_map", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        launchActivity<MapActivity>()

        onView(withId(R.id.mapView)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomSheet)).check(matches(not(isDisplayed())))


    }


}