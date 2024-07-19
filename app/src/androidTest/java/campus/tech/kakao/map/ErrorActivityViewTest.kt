package campus.tech.kakao.map

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import campus.tech.kakao.map.view.ErrorActivity
import androidx.test.core.app.ApplicationProvider.getApplicationContext

@RunWith(AndroidJUnit4::class)
@LargeTest
class ErrorActivityViewTest {

    private lateinit var activityScenario: ActivityScenario<ErrorActivity>

    @Before
    fun setUp() {
        // Intent를 사용하여 ErrorActivity를 시작하고 오류 메시지를 전달
        val intent = Intent(getApplicationContext(), ErrorActivity::class.java).apply {
            putExtra("error", "API 인증에 실패")
        }
        activityScenario = ActivityScenario.launch(intent)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun testErrorMessageDisplayed() {
        // 오류 메시지가 제대로 표시되는지 확인
        onView(withId(R.id.errorMessage))
            .check(matches(withText(R.string.error_message)))

        // 오류 내용이 제대로 표시되는지 확인
        onView(withId(R.id.errorContent))
            .check(matches(withText("API 인증에 실패")))

        Thread.sleep(2000L)
    }
}
