package campus.tech.kakao.map

import campus.tech.kakao.map.data.ServerResult
import campus.tech.kakao.map.data.repository.ResultRepositoryImpl
import campus.tech.kakao.map.data.source.RetrofitService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ResultTest {
    lateinit var resultRepositoryImpl: ResultRepositoryImpl
    lateinit var retrofit: RetrofitService

    @Before
    fun init() {
        retrofit = mockk()
        resultRepositoryImpl = ResultRepositoryImpl(retrofit)
    }

    @Test
    fun `필수 query인 keyword가 없는경우 서버에 요청하지 않음`() = runTest {
        val emptyKeyword = ""
        coEvery {
            retrofit.requestLocationByKeyword("KEY", emptyKeyword)
        } returns Response.success(null)

        resultRepositoryImpl.search(emptyKeyword)

        coVerify(exactly = 0) {
            retrofit.requestLocationByKeyword("KEY", emptyKeyword)
        }
    }
}

