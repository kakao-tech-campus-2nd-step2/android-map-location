package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.map
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class FunTest {
	private lateinit var model: MainViewModel
	private lateinit var viewModelStore: ViewModelStore
	private lateinit var context: Context
	@Before
	fun setUp() {
		context = RuntimeEnvironment.getApplication()
		viewModelStore = ViewModelStore()
		model = MainViewModel(context as Application)
	}
	@Test
	fun 검색어를_입력하면_검색_결과_표시(){
		val query = "이안아파트"
		val expectedQueryResultName = arrayOf("이안금곡아파트", "대우이안아파트", "이안금곡아파트 관리사무소",
			"대우이안아파트 정문", "이안동래센트럴시티아파트", "이안금곡아파트 입주자대표회의 전기차충전소",
			"대우이안아파트 상가동", "CU 화명대우이안점", "대우이안아파트 지하주차장",
			"이안공인중개사사무소", "부산시 북구 대우이안아파트 전기차충전소", "대우이안공인중개사사무소",
			"삼계이안아파트", "이안센트럴포레장유1단지아파트", "성일이안시티아파트")
		val expectedQueryResultCount = 15
		model.searchLocalAPI(query)
		val actualQueryResult = model.documentList.value
		actualQueryResult?.forEach { document ->
			assertTrue(expectedQueryResultName.contains(document.placeName))
		}

	}
}