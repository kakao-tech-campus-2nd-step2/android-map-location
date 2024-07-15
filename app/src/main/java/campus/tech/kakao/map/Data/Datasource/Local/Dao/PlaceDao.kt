package campus.tech.kakao.map.Data.Datasource.Local.Dao

import campus.tech.kakao.map.Domain.Model.Place

interface PlaceDao {
    fun deletePlace(name: String)
    fun getSimilarPlacesByName(name: String) : List<Place>
    fun getPlaceByName(name: String): Place
}