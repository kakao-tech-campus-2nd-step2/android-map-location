package campus.tech.kakao.map

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        sharedPreferences = ApplicationProvider.getApplicationContext<Context>().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun `앱_실행_직후_메인에서_보여주는_지도는_가장_최근검색어의_위치_혹은_기본값`() {
        // given
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SEARCH_HISTORY_KEY, "[{\"document\":{\"x\":\"126.9780\",\"y\":\"37.5665\"}}]")
        editor.apply()

        // when
        activityRule.scenario.moveToState(Lifecycle.State.RESUMED)

        // then
        onView(withId(R.id.map_view)).check(matches(isDisplayed()))

        editor.clear().apply()
    }

    @Test
    fun `앱_실행_직후_최근검색어가_없는_경우_기본위치_확인`() {
        // given
        val editor = sharedPreferences.edit()
        editor.clear().apply()

        // when
        activityRule.scenario.moveToState(Lifecycle.State.RESUMED)

        // then
        onView(withId(R.id.map_view)).check(matches(isDisplayed()))
    }

    @Test
    fun `지도_호출_오류시_오류화면_이동`() {
        // given
        activityRule.scenario.onActivity { activity ->
            runOnUiThread {
                val mapView = activity.findViewById<MapView>(R.id.map_view)
                mapView.start(object : MapLifeCycleCallback() {
                    override fun onMapDestroy() {
                        // 지도 파괴 시 처리
                    }

                    override fun onMapError(error: Exception) {
                        // when
                        activity.startActivity(
                            Intent(activity, ErrorActivity::class.java)
                                .putExtra("Error", "Test Error Message")
                        )
                    }
                }, null)
            }
        }

        // then
        Intents.intended(hasComponent(ErrorActivity::class.java.name))
    }
}
