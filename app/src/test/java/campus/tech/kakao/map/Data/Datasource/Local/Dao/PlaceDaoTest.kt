package campus.tech.kakao.map.Data.Datasource.Local.Dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.Data.Datasource.Local.SqliteDB
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.Domain.Model.PlaceCategory
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest= Config.NONE)
class PlaceDaoTest {
    private lateinit var placeDao: PlaceDao
    private lateinit var fakeDB: SqliteDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        fakeDB = SqliteDB(context, "test",null,1)
        placeDao = PlaceDaoImpl(fakeDB.writableDatabase)
    }

    @After
    fun closeDb() {
        fakeDB.close()
    }

    @Test
    fun `검색한 키워드로 장소를 불러온다`(){
        val resultCafe = placeDao.getSimilarPlacesByName("카페")
        val resultRestaurant = placeDao.getSimilarPlacesByName("음식점")
        val resultPharmacy = placeDao.getSimilarPlacesByName("약국")

        assertTrue(resultCafe.all{it.category == PlaceCategory.CAFE})
        assertTrue(resultRestaurant.all{it.category == PlaceCategory.RESTAURANT})
        assertTrue(resultPharmacy.all{it.category == PlaceCategory.PHARMACY})
    }

}