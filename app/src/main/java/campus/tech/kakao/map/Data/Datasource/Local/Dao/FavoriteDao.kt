package campus.tech.kakao.map.Data.Datasource.Local.Dao

import campus.tech.kakao.map.Domain.Model.Place

interface FavoriteDao {
    fun getCurrentFavorite(): MutableList<Place>
    fun deleteFavorite(id : Int)
    fun addFavorite(place: Place)
    fun getFavoriteById(id: Int): Place?
}