package campus.tech.kakao.map.Data.Datasource.Local.Dao

import campus.tech.kakao.map.Domain.Model.Place

interface PlaceDao {
    fun deletePlace(id : Int)
    fun getSimilarPlacesByName(name: String) : List<Place>
    fun getPlaceById(id : Int): Place
}