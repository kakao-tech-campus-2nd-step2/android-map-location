package campus.tech.kakao.map.Domain

import campus.tech.kakao.map.Domain.Model.Place

interface PlaceRepository {
    fun getCurrentFavorite() : List<Place>
    fun getSimilarPlacesByName(name: String) : List<Place>
    fun addFavorite(place : Place) : List<Place>
    fun getPlaceById(id : Int): Place?
    fun getFavoriteById(id: Int) : Place?
    fun deleteFavorite(id : Int) : List<Place>
    suspend fun searchPlaceRemote(name : String) : List<Place>
    fun getPlaceByNameHTTP(name : String) : List<Place>
}