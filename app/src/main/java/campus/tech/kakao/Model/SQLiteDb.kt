package campus.tech.kakao.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.Model.SQLiteHelper.Companion.COL_ID_2
import campus.tech.kakao.Model.SQLiteHelper.Companion.COL_NAME_2

class SQLiteDb(context: Context) {
    private val dbHelper: SQLiteHelper = SQLiteHelper.getInstance(context)
    private val database: SQLiteDatabase = dbHelper.writableDatabase

    fun insertIntoSelectedData(name: String): Long {
        val values = ContentValues().apply {
            put(COL_NAME_2, name)
        }
        return database.insert(SQLiteHelper.TABLE_NAME_2, null, values)
    }

    fun getAllSelectedData(): List<Pair<Int, String>> {
        val selectedData = mutableListOf<Pair<Int, String>>()
        val cursor: Cursor = database.query(
            SQLiteHelper.TABLE_NAME_2,
            arrayOf(COL_ID_2, COL_NAME_2),
            null,
            null,
            null,
            null,
            "$COL_ID_2 DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COL_ID_2))
                val name = getString(getColumnIndexOrThrow(COL_NAME_2))
                selectedData.add(id to name)
            }
        }

        cursor.close()
        return selectedData
    }

    fun deleteFromSelectedData(id: Int): Int {
        return database.delete(
            SQLiteHelper.TABLE_NAME_2,
            "$COL_ID_2 = ?",
            arrayOf(id.toString())
        )
    }
}
