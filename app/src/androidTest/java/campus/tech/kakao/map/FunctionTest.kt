package campus.tech.kakao.map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class FunctionTest {

    private lateinit var scenarioMain: ActivityScenario<MainActivity>
    private lateinit var scenarioSearch: ActivityScenario<SearchActivity>
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        scenarioMain = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenarioMain.close()
        if (::scenarioSearch.isInitialized) {
            scenarioSearch.close()
        }
    }
}