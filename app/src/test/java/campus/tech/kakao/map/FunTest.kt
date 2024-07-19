package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelStore
import campus.tech.kakao.map.dbHelper.SearchWordDbHelper
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.SearchWord
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
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
		model.searchLocalAPI(query)
		val actualQueryResult = model.documentList.value
		actualQueryResult?.forEach { document ->
			assert(expectedQueryResultName.contains(document.placeName))
		}
	}

	@Test
	fun 검색어_저장_되는지_확인(){
		val query = Document(
			"이안아파트", "아파트",
			"남양주", "10",
			"10")
		val expectedResult = SearchWord(
			"이안아파트", "남양주", "아파트")
		model.addWord(query)
		val result = model.wordList.value?.contains(expectedResult)
		if (result != null) assert(result) else assert(false)
	}
}