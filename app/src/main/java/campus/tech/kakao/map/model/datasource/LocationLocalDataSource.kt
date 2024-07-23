package campus.tech.kakao.map.model.datasource

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import campus.tech.kakao.map.model.Contract.LocationEntry
import campus.tech.kakao.map.model.Contract.SavedLocationEntry
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.LocationDbHelper
import campus.tech.kakao.map.model.SavedLocation

class LocationLocalDataSource(private val dbHelper : LocationDbHelper) {

    fun addSavedLocation(title: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SavedLocationEntry.COLUMN_NAME_TITLE, title)
        }
        Log.d("jieun", "insertSavedLocation 저장완료")
        return db.insert(SavedLocationEntry.TABLE_NAME, null, values)
    }

    fun getSavedLocationAll(): MutableList<SavedLocation> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            SavedLocationEntry.COLUMN_NAME_TITLE
        )
        val sortOrder = "${SavedLocationEntry.COLUMN_NAME_TITLE} ASC"
        val cursor = db.query(
            SavedLocationEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val results = mutableListOf<SavedLocation>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(SavedLocationEntry.COLUMN_NAME_TITLE))
                results.add(SavedLocation(title))
            }
        }
        cursor.close()
        return results
    }

    fun deleteSavedLocation(title: String): Int {
        val db = dbHelper.writableDatabase

        val selection = "${SavedLocationEntry.COLUMN_NAME_TITLE} = ?"
        val selectionArgs = arrayOf(title)

        return db.delete(SavedLocationEntry.TABLE_NAME, selection, selectionArgs)
    }
}
