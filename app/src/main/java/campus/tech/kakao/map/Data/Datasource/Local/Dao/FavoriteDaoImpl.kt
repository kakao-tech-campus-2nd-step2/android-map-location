package campus.tech.kakao.map.Data.Datasource.Local.Dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.Domain.Model.PlaceContract

class FavoriteDaoImpl(private val db : SQLiteDatabase) : FavoriteDao {

    override fun getCurrentFavorite(): MutableList<Place> {
        val cursor = db.rawQuery("SELECT * FROM ${PlaceContract.FavoriteEntry.TABLE_NAME}", null)
        return PlaceContract.getPlaceListByCursor(cursor).toMutableList()
    }

    override fun deleteFavorite(id : Int) {
        db.delete(
            PlaceContract.FavoriteEntry.TABLE_NAME,
            "${PlaceContract.PlaceEntry.COLUMN_ID}=?",
            arrayOf(id.toString())
        )
    }

    override fun addFavorite(place: Place) {
        val values = ContentValues().apply {
            this.put(PlaceContract.PlaceEntry.COLUMN_ID, place.id)
            this.put(PlaceContract.PlaceEntry.COLUMN_NAME, place.name)
            this.put(PlaceContract.PlaceEntry.COLUMN_ADDRESS, place.address)
            this.put(PlaceContract.PlaceEntry.COLUMN_CATEGORY, place.category?.ordinal)
        }

        db.insert(PlaceContract.FavoriteEntry.TABLE_NAME, null, values)
    }

    override fun getFavoriteById(id: Int): Place {
        val cursor = db.rawQuery(
            "SELECT * FROM ${PlaceContract.FavoriteEntry.TABLE_NAME} WHERE id=?",
            arrayOf(id.toString())
        )
        return PlaceContract.getPlaceByCursor(cursor)
    }
}