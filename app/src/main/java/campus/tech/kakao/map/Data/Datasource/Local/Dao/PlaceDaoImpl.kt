package campus.tech.kakao.map.Data.Datasource.Local.Dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.Domain.Model.PlaceContract

class PlaceDaoImpl(private val db : SQLiteDatabase) : PlaceDao {

    override fun deletePlace(id : Int) {
        db.delete(
            PlaceContract.PlaceEntry.TABLE_NAME,
            "${PlaceContract.PlaceEntry.COLUMN_ID}=?",
            arrayOf(id.toString())
        )
    }

    override fun getSimilarPlacesByName(name: String): List<Place> {
        val cursor = getSimilarCursorByName(name)
        return PlaceContract.getPlaceListByCursor(cursor)
    }

    override fun getPlaceById(id: Int): Place {
        val cursor = getCursorById(id)
        return PlaceContract.getPlaceByCursor(cursor)
    }

    private fun getAllPlace(): List<Place> {
        val cursor = db.rawQuery("SELECT * FROM ${PlaceContract.PlaceEntry.TABLE_NAME}", null)
        return PlaceContract.getPlaceListByCursor(cursor)
    }

    private fun getSimilarCursorByName(name : String) : Cursor{
        val selection = "${PlaceContract.PlaceEntry.COLUMN_NAME} LIKE ?"
        val selectionArgs = arrayOf("%$name%")

        return db.query(
            PlaceContract.PlaceEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
    }

    private fun getCursorById(id: Int): Cursor {
        return db.rawQuery(
            "SELECT * FROM ${PlaceContract.PlaceEntry.TABLE_NAME} WHERE id=?",
            arrayOf(id.toString())
        )
    }
}