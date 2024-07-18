package campus.tech.kakao.map.data.repository

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import campus.tech.kakao.map.MapContract
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.domain.repository.LastLocationRepository

class LastLocationRepositoryImpl(
    private val helper: MapDbHelper
) : LastLocationRepository {
    override fun insertLastLocation(location: Location) {
        val writeableDb = helper.writableDatabase
        val content = locationToContent(location)
        clearLastLocation()
        writeableDb.insert(MapContract.MapEntry.TABLE_NAME_LAST_LOCATION, null, content)
    }

    override fun clearLastLocation() {
        val writeableDb = helper.writableDatabase
        helper.clearLastLocation(writeableDb)
    }

    override fun getLastLocation(): Location? {
        val readableDb = helper.readableDatabase
        val cursor = readableDb.query(
            MapContract.MapEntry.TABLE_NAME_LAST_LOCATION,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return if(cursor.moveToNext())
            cursorToLocation(cursor)
        else
            null
    }

    private fun locationToContent(location: Location): ContentValues {
        val content = ContentValues()
        content.put(MapContract.MapEntry.COLUMN_NAME_ID, location.id)
        content.put(MapContract.MapEntry.COLUMN_NAME_NAME, location.name)
        content.put(MapContract.MapEntry.COLUMN_NAME_CATEGORY, location.category)
        content.put(MapContract.MapEntry.COLUMN_NAME_ADDRESS, location.address)
        content.put(MapContract.MapEntry.COLUMN_NAME_X, location.x)
        content.put(MapContract.MapEntry.COLUMN_NAME_Y, location.y)

        return content
    }

    private fun cursorToLocation(cursor: Cursor): Location {
        val id =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_ID))
        val name =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_NAME))
        val category =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_CATEGORY))
        val address =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_ADDRESS))
        val x =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_X)).toDouble()
        val y =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_Y)).toDouble()

        return Location(id, name, category, address, x, y)
    }
}