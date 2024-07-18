package campus.tech.kakao.map.data.repository

import android.content.ContentValues
import android.database.Cursor
import campus.tech.kakao.map.MapContract
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val helper: MapDbHelper
) : HistoryRepository {
    override fun insertHistory(newHistory: Location) {
        if (isHistoryExist(newHistory))
            deleteHistory(newHistory)
        val writeableDb = helper.writableDatabase
        val content = historyToContent(newHistory)

        writeableDb.insert(MapContract.MapEntry.TABLE_NAME_HISTORY, null, content)
    }

    override fun deleteHistory(oldHistory: Location) {
        val writeableDb = helper.writableDatabase
        val selection = "${MapContract.MapEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(oldHistory.id)

        writeableDb.delete(MapContract.MapEntry.TABLE_NAME_HISTORY, selection, selectionArgs)
    }

    override fun getAllHistory(): List<Location> {
        val readableDb = helper.readableDatabase
        val cursor = readableDb.query(
            MapContract.MapEntry.TABLE_NAME_HISTORY,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val res = mutableListOf<Location>()
        while (cursor.moveToNext()) {
            res.add(cursorToHistory(cursor))
        }
        cursor.close()
        return res
    }

    private fun isHistoryExist(newHistory: Location): Boolean {
        val readableDb = helper.readableDatabase
        val selection = "${MapContract.MapEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(newHistory.id)
        val cursor = readableDb.query(
            MapContract.MapEntry.TABLE_NAME_HISTORY,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val isExist: Boolean = cursor.moveToNext()
        cursor.close()
        return isExist
    }

    private fun historyToContent(location: Location): ContentValues {
        val content = ContentValues()
        content.put(MapContract.MapEntry.COLUMN_NAME_ID, location.id)
        content.put(MapContract.MapEntry.COLUMN_NAME_NAME, location.name)
        content.put(MapContract.MapEntry.COLUMN_NAME_CATEGORY, location.category)
        content.put(MapContract.MapEntry.COLUMN_NAME_ADDRESS, location.address)
        content.put(MapContract.MapEntry.COLUMN_NAME_X, location.x)
        content.put(MapContract.MapEntry.COLUMN_NAME_Y, location.y)

        return content
    }

    private fun cursorToHistory(cursor: Cursor): Location {
        val id =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_ID))
        val name =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_NAME))
        val category =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_CATEGORY))
        val address =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_ADDRESS))
        val x =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_X))
                .toDouble()
        val y =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_Y))
                .toDouble()

        return Location(id, name, category, address, x, y)
    }
}