package campus.tech.kakao.map.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import campus.tech.kakao.map.domain.PlaceDataModel

class PlaceDatabaseAccess(context: Context, databaseName: String) {
    private val dbHelper = PlaceDatabaseHelper(context, databaseName)

    fun insertPlace(place: PlaceDataModel) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PlaceContract.Place.COLUMN_NAME, place.name)
            place.address?.let { put(PlaceContract.Place.COLUMN_ADDRESS, it) }
            place.category?.let { put(PlaceContract.Place.COLUMN_CATEGORY, it) }
            place.x?.let { put(PlaceContract.Place.COLUMN_X, it) }
            place.y?.let { put(PlaceContract.Place.COLUMN_Y, it) }
        }
        db.insert(PlaceContract.Place.TABLE_NAME, null, values)
    }

    fun deletePlace(name: String) {
        val db = dbHelper.writableDatabase
        db.delete(PlaceContract.Place.TABLE_NAME, "${PlaceContract.Place.COLUMN_NAME} = ?", arrayOf(name))
    }

    fun getAllPlace(): MutableList<PlaceDataModel> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${PlaceContract.Place.TABLE_NAME}", null)
        val dataList = mutableListOf<PlaceDataModel>()

        if (cursor.moveToFirst()) {
            do {
                val nameIndex = cursor.getColumnIndex(PlaceContract.Place.COLUMN_NAME)
                val addressIndex = cursor.getColumnIndex(PlaceContract.Place.COLUMN_ADDRESS)
                val categoryIndex = cursor.getColumnIndex(PlaceContract.Place.COLUMN_CATEGORY)
                val xIndex = cursor.getColumnIndex(PlaceContract.Place.COLUMN_X)
                val yIndex = cursor.getColumnIndex(PlaceContract.Place.COLUMN_Y)

                val name = cursor.getString(nameIndex)
                val address = if (addressIndex != -1) cursor.getString(addressIndex) else null
                val category = if (categoryIndex != -1) cursor.getString(categoryIndex) else null
                val x = if (xIndex != -1) cursor.getString(xIndex) else null
                val y = if (yIndex != -1) cursor.getString(yIndex) else null

                dataList.add(PlaceDataModel(name, address, category, x, y))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }
}