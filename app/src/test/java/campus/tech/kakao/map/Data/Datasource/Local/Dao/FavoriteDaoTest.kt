package campus.tech.kakao.map.Data.Datasource.Local.Dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.Data.Datasource.Local.SqliteDB
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.Domain.Model.PlaceCategory
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner::class)
class FavoriteDaoTest {
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var fakeDB: SqliteDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        fakeDB = SqliteDB(context, "test",null,1)
        favoriteDao = FavoriteDaoImpl(fakeDB.writableDatabase)
    }

    @After
    fun closeDb() {
        fakeDB.close()
    }

    @Test
    fun `id값으로 즐겨찾기 가져오기`() {
        val fakePlace = Place(1, "name", "address", PlaceCategory.CAFE,1.0, 1.0)
        favoriteDao.addFavorite(fakePlace)

        favoriteDao.getFavoriteById(fakePlace.id).let{
            assertEquals(fakePlace,it)
        }
    }

    @Test
    fun `존재하지 않는 항목이나 삭제된 항목을 찾을 시 null 반환`(){
        val fakePlace = Place(1, "name", "address", PlaceCategory.CAFE,1.0, 1.0)
        favoriteDao.addFavorite(fakePlace)
        favoriteDao.deleteFavorite(fakePlace.id)

        assertEquals(null, favoriteDao.getFavoriteById(fakePlace.id))
        assertEquals(null,favoriteDao.getFavoriteById(-1))
    }

    @Test
    fun `즐겨찾기 추가`(){
        val before = favoriteDao.getCurrentFavorite()
        val fakePlace = Place(1, "name", "address", PlaceCategory.CAFE,1.0, 1.0)
        favoriteDao.addFavorite(fakePlace)
        val after = favoriteDao.getCurrentFavorite()

        before.add(fakePlace)
        assertEquals(before,after)
    }
}