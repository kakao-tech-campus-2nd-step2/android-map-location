package campus.tech.kakao.map.Data

import campus.tech.kakao.map.Data.Datasource.Local.Dao.FavoriteDao
import campus.tech.kakao.map.Data.Datasource.Local.Dao.PlaceDao
import campus.tech.kakao.map.Data.Datasource.Remote.RemoteService
import campus.tech.kakao.map.Data.Datasource.Remote.Response.Document
import campus.tech.kakao.map.Data.Datasource.Remote.Response.toVO
import campus.tech.kakao.map.Data.Datasource.Remote.RetrofitService
import campus.tech.kakao.map.Domain.PlaceRepository
import campus.tech.kakao.map.Domain.Model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepositoryImpl(
    private val placeDao: PlaceDao,
    private val favoriteDao: FavoriteDao,
    private val retrofitService: RetrofitService,
    private val httpUrlConnect: RemoteService
) : PlaceRepository {

    override fun getCurrentFavorite() : List<Place>{
        return favoriteDao.getCurrentFavorite()
    }

    override fun getSimilarPlacesByName(name: String) : List<Place> {
        return placeDao.getSimilarPlacesByName(name)
    }

    override fun getPlaceByName(name: String): Place {
        return placeDao.getPlaceByName(name)
    }

    override fun addFavorite(place : Place) : List<Place> {
        favoriteDao.addFavorite(place)
        return getCurrentFavorite()
    }

    override fun deleteFavorite(name: String) : List<Place>{
        favoriteDao.deleteFavorite(name)
        return getCurrentFavorite()
    }

    override suspend fun searchPlaceRemote(name: String) : List<Place>{
        return getPlaceByNameRemote(name)
    }

    override fun getPlaceByNameHTTP(name : String) : List<Place>{
        val places = mutableListOf<Place>()
        httpUrlConnect.getPlaceResponse(name).forEach{
            places.add(
                it.toVO()
            )
        }
        return places
    }

    private suspend fun getPlaceByNameRemote(name: String): List<Place> =
        withContext(Dispatchers.IO) {
            val pageCount = getPageCount(name)
            var placeList: MutableList<Place> = mutableListOf<Place>()

            for (page in 1..pageCount) {
                val req = retrofitService.requestProducts(query = name, page = page).execute()

                when (req.code()) {
                    200 -> {
                        req.body()?.documents?.forEach {
                            placeList.add(
                                it.toVO()
                            )
                        }
                    }
                    else -> {}
                }

            }
            placeList
        }

    private fun getPageCount(name: String): Int {
        val req = retrofitService.requestProducts(query = name).execute()

        when (req.code()) {
            200 -> return minOf(
                MAX_PAGE, req.body()?.meta?.pageableCount ?: 1
            )

            else -> return 0
        }
    }

    companion object {
        const val MAX_PAGE = 2
    }
}