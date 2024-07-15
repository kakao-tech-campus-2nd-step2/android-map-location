package campus.tech.kakao.map.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class PlaceDBHelper(context: Context) : SQLiteOpenHelper(context, "placedb", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(PlaceContract.SavePlaceEntry.CREATE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(PlaceContract.SavePlaceEntry.DROP_QUERY)
        onCreate(db)
    }

    fun savePlaces(placeName: String) {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(PlaceContract.SavePlaceEntry.COLUMN_PLACE_NAME, placeName)

        val cursor = db.query(
            PlaceContract.SavePlaceEntry.TABLE_NAME,
            arrayOf(PlaceContract.SavePlaceEntry.COLUMN_PLACE_NAME),
            "${PlaceContract.SavePlaceEntry.COLUMN_PLACE_NAME} = ?",
            arrayOf(placeName),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            db.delete(
                PlaceContract.SavePlaceEntry.TABLE_NAME,
                "${PlaceContract.SavePlaceEntry.COLUMN_PLACE_NAME} = ?",
                arrayOf(placeName)
            )
        }
        cursor.close()
        db.insert(PlaceContract.SavePlaceEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun showSavePlace(): MutableList<SavePlace> {
        val db: SQLiteDatabase = this.readableDatabase
        val savePlaces = mutableListOf<SavePlace>()
        var cursor: Cursor? = null
        try {
            cursor = db.query(
                PlaceContract.SavePlaceEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
            )

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.SavePlaceEntry.COLUMN_PLACE_NAME))

                    savePlaces.add(SavePlace(name))
                }
            }
        } catch (e: Exception) {
            Log.e("ddangcong80", "Error", e)
        } finally {
            cursor?.close()
            db.close()
        }

        return savePlaces
    }

    fun deleteSavedPlace(savedPlaceName: String) {
        val db: SQLiteDatabase = this.writableDatabase

        val cursor = db.query(
            PlaceContract.SavePlaceEntry.TABLE_NAME,
            arrayOf(PlaceContract.SavePlaceEntry.COLUMN_PLACE_NAME),
            "${PlaceContract.SavePlaceEntry.COLUMN_PLACE_NAME} = ?",
            arrayOf(savedPlaceName),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            db.delete(
                PlaceContract.SavePlaceEntry.TABLE_NAME,
                "${PlaceContract.SavePlaceEntry.COLUMN_PLACE_NAME} = ?",
                arrayOf(savedPlaceName)
            )
        }
        cursor.close()
        db.close()
    }
}