package campus.tech.kakao.map.Data

import campus.tech.kakao.map.Data.Datasource.Local.Dao.FavoriteDao
import campus.tech.kakao.map.Data.Datasource.Local.Dao.PlaceDao
import campus.tech.kakao.map.Data.Datasource.Remote.HttpUrlConnect
import campus.tech.kakao.map.Data.Datasource.Remote.RetrofitService
import campus.tech.kakao.map.Domain.Model.PlaceCategory
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceRepositoryTest{
    private lateinit var retrofitService: RetrofitService
    private lateinit var placeRepository: PlaceRepositoryImpl
    @Before
    fun createDb() {
        retrofitService = Retrofit.Builder()
            .baseUrl(RetrofitService.BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
        placeRepository = PlaceRepositoryImpl(mockk<PlaceDao>(),mockk<FavoriteDao>(),retrofitService,HttpUrlConnect())
    }

    @Test
    fun `카카오 API 활용해서 키워드 검색(Retrofit)`(){
        CoroutineScope(Dispatchers.IO).launch {
            val resultCafe = placeRepository.searchPlaceRemote("카페")
            val resultRestaurant = placeRepository.searchPlaceRemote("음식점")
            val resultPharmacy = placeRepository.searchPlaceRemote("약국")

            assertTrue(resultCafe.all{it.category == PlaceCategory.CAFE})
            assertTrue(resultRestaurant.all{it.category == PlaceCategory.RESTAURANT})
            assertTrue(resultPharmacy.all{it.category == PlaceCategory.PHARMACY})
        }
    }

    @Test
    fun `카카오 API 활용해서 키워드 검색(HttpUrlConnection)`(){
        val resultCafe = placeRepository.getPlaceByNameHTTP("카페")
        val resultRestaurant = placeRepository.getPlaceByNameHTTP("음식점")
        val resultPharmacy = placeRepository.getPlaceByNameHTTP("약국")

        assertTrue(resultCafe.all{it.category == PlaceCategory.CAFE})
        assertTrue(resultRestaurant.all{it.category == PlaceCategory.RESTAURANT})
        assertTrue(resultPharmacy.all{it.category == PlaceCategory.PHARMACY})
    }

    @Test
    fun `검색 중 가능할 시 15개 이상의 데이터를 보여준다(Retrofit)`(){
        CoroutineScope(Dispatchers.IO).launch{
            assertTrue(placeRepository.searchPlaceRemote("카페").size > 15)
        }
    }

    @Test
    fun `검색 중 가능할 시 15개 이상의 데이터를 보여준다(HttpUrlConnection)`(){
        assertTrue(placeRepository.getPlaceByNameHTTP("카페").size > 15)
    }



}