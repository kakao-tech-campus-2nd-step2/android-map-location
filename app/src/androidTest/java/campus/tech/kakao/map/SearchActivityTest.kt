package campus.tech.kakao.map

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class SearchActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SearchActivity::class.java)

    private lateinit var sharedPreferences: SharedPreferences
    private var idlingResource: IdlingResource? = null
    private lateinit var placeAdapter: PlaceAdapter

    @Before
    fun setUp() {
        sharedPreferences = ApplicationProvider.getApplicationContext<Context>().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
        idlingResource?.let { IdlingRegistry.getInstance().unregister(it) }
    }

    @Test
    fun `검색창_X누르면_지워진다`() {
        onView(withId(R.id.search)).perform(typeText("cafe"))
        onView(withId(R.id.xmark)).perform(click())
        onView(withId(R.id.search)).check(matches(withText("")))
    }

//    @Test
//    fun `검색결과_누르면_메인액티비티로_이동한다`() {
//        // Activity 실행 시점에 RecyclerView의 IdlingResource 등록
//        activityRule.scenario.onActivity { activity ->
//            val recyclerView = activity.findViewById<RecyclerView>(R.id.placeResult)
//            idlingResource = RecyclerViewIdlingResource(recyclerView)
//            IdlingRegistry.getInstance().register(idlingResource)
//
//            // 수동으로 RecyclerView에 데이터 삽입
//            val testData = listOf(
//                Document(
//                    addressName = "서울 강남구 삼성동 159",
//                    categoryGroupCode = "",
//                    categoryGroupName = "",
//                    categoryName = "가정,생활 > 문구,사무용품 > 디자인문구 > 카카오프렌즈",
//                    distance = "418",
//                    id = "26338954",
//                    phone = "02-6002-1880",
//                    placeName = "카카오프렌즈 코엑스점",
//                    placeUrl = "http://place.map.kakao.com/26338954",
//                    roadAddressName = "서울 강남구 영동대로 513",
//                    x = "127.05902969025047",
//                    y = "37.51207412593136"
//                )
//            )
//
//            val placeAdapter = PlaceAdapter(
//                testData,
//                LayoutInflater.from(activity),
//                object : PlaceAdapter.OnItemClickListener {
//                    override fun onItemClick(position: Int) {
//                        val item = placeAdapter.getItem(position)
//                        val intent = Intent(activity, MainActivity::class.java)
//                        intent.putExtra("longitude", item.x)
//                        intent.putExtra("latitude", item.y)
//                        intent.putExtra("name", item.placeName)
//                        intent.putExtra("address", item.addressName)
//                        activity.startActivity(intent)
//                    }
//                })
//
//            recyclerView.adapter = placeAdapter
//            placeAdapter.notifyDataSetChanged()
//        }
//
//        // RecyclerView 데이터 로드 확인
//        onView(withId(R.id.placeResult)).check(matches(hasMinimumChildCount(1)))
//
//        // 첫 번째 아이템 클릭
//        onView(withId(R.id.placeResult)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
//
//        // MainActivity로 이동 확인
//        Intents.intended(hasComponent(MainActivity::class.java.name))
//    }
}